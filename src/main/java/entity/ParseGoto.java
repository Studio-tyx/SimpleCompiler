package entity;

import exception.TYXException;
import tool.CharacterTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author TYX
 * @name ParseGoto
 * @description 从DFA图结构转换为Goto表
 * @createTime 2021/3/21 14:20
 **/
public class ParseGoto {
    private GOTO gotoTable;
    private Text text;  //语法

    /**
     * 成员变量初始化<br>
     *     由语法分析终结符与非终结符
     *
     * @param text 语法读入 Text
     */
    public void init(Text text) {
        this.text = text;
        List<Character> terminals = new ArrayList<Character>(); //终结符
        List<Character> nonTerminals = new ArrayList<Character>();  //非终结符

        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();

            if (line.contains("#")) {   //分割语义文法与语法文法
                String[] content = line.split("#");
                line = content[0];
            }

            for (int i = 0; i < line.length(); i++) {   //统计终结符与非终结符
                if (i == 1 || i == 2) continue;
                Character character = line.charAt(i);
                if (CharacterTools.isUpper(character)) {
                    if (!nonTerminals.contains(character)) nonTerminals.add(character); //非终结符
                } else {
                    if (!terminals.contains(character) && character != '@') terminals.add(character);   //终结符不含空字符
                }
            }

        }
        terminals.add('#');
        gotoTable=new GOTO(terminals,nonTerminals);
    }

    /**
     * 由DFA转换为Goto表
     *
     * @param parseDFA 输入的DFA ParseDFA
     * @throws TYXException 非LR(1)文法报错
     */
    public void createGoto(ParseDFA parseDFA) throws TYXException {
        int statusNumber = parseDFA.getStatus().size(); //状态数（用以建goto表）
        gotoTable.setStatusNumber(statusNumber);
        List<Character> terminals= gotoTable.getTerminals();  //终结符集合
        List<Character> nonTerminals= getGotoTable().getNonTerminals();   //非终结符集合

        String[][] Goto;
        Goto = new String[statusNumber][terminals.size() + nonTerminals.size()];    //goto表
        LinkGraph<Set<LRLine>, Character> graph = parseDFA.getGraph();  //状态转换图

        for (int i = 0; i < statusNumber; i++) {  //对于每个状态（表中每行）
            for (int j = 0; j < terminals.size() + nonTerminals.size(); j++) {  //每行表格初始化（便于后续判断是否为LR1文法）
                Goto[i][j] = "n";
            }
            Set<LRLine> thisSet = parseDFA.getStatus().get(i);  //当前状态
            //ShowTools.show(thisSet);
            for (LRLine lrLine : thisSet) {
                if (lrLine.getStart() == '%' && lrLine.getContent().equals("S·")) { //设置%->S·为acc
                    if (!Goto[i][terminals.size() - 1].equals("n")) //重复写 说明非LR1文法
                        throw new TYXException("Input grammar error:Not a LR(1) grammar!");
                    else Goto[i][terminals.size() - 1] = "acc";
                }
                if (parseDFA.endProduction(lrLine.getContent())) { //判断是否有到底的句子->r规约
                    for (Character forwardSearch : lrLine.getForwardSearch()) {
                        if (lrLine.getProductionNumber() != 0) {    //'S->S 第0号产生式不算
                            if (!Goto[i][terminals.indexOf(forwardSearch)].equals("n")) //重复写 说明非LR1文法
                                throw new TYXException("Input grammar error:Not a LR(1) grammar!");
                            else Goto[i][terminals.indexOf(forwardSearch)] = "r" + lrLine.getProductionNumber();
                        }
                    }
                }
            }
            for (int j = 0; j <terminals.size(); j++) {    //对于每个终结符 判断是否可以到达新的状态
                Set<LRLine> nextSet = graph.findNextVertex(thisSet, terminals.get(j));  //下一状态
                if (nextSet != null) {
                    if (!Goto[i][j].equals("n")) {
                        //重复写 说明非LR1文法
                        throw new TYXException("Input grammar error:Not a LR(1) grammar! Former:i:" + i + "," + "j:" + j + ",content:" + Goto[i][j]);
                    } else Goto[i][j] = "S" + parseDFA.getIndex(parseDFA.getStatus(), nextSet); //goto表构造
                }
            }
            for (int j = 0; j < nonTerminals.size(); j++) {    //对于每个非终结符 判断下一个状态
                Set<LRLine> nextSet = graph.findNextVertex(thisSet, nonTerminals.get(j));   //下一状态
                if (nextSet != null) {
                    if (!Goto[i][j + terminals.size()].equals("n")) //重复写 说明非LR1文法
                        throw new TYXException("Input grammar error:Not a LR(1) grammar!");
                    else
                        Goto[i][j + terminals.size()] = String.valueOf(parseDFA.getIndex(parseDFA.getStatus(), nextSet));   //goto表构造
                }
            }
        }
        gotoTable.setGoto(Goto);
    }

    public GOTO getGotoTable() {
        return gotoTable;
    }

    /**
     * 根据tokens判断是否符合语法
     *
     * @param tokens token集合 Tokens
     * @return 语法分析结果 ParseResult
     * @throws TYXException 非LR1文法异常
     */
    public ParseResult check(Tokens tokens) throws TYXException {
        Tokens thisTokens = tokens; //把形参变成实参 主要是也没啥必要写一遍setTokens
        boolean res = false;    //结果
        Stack<Integer> status = new Stack<Integer>();   //状态栈
        Stack<Character> characters = new Stack<Character>();   //符号栈
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();   //现有的语法树（或语法子树）
        int countTemp = 0;  //临时变量计数器
        List<ParseResult.Line> lines=new ArrayList<ParseResult.Line>(); //分析栈
        List<String> quaternions=new ArrayList<String>();   //四元式
        List<Character> terminals= gotoTable.getTerminals();  //终结符集合
        List<Character> nonTerminals= getGotoTable().getNonTerminals();   //非终结符集合
        StringBuilder information= new StringBuilder(); //分析信息

        //初态
        status.push(0);
        characters.push('#');

        //tokens尾部加#便于判断是否结束
        Token end = new Token(thisTokens.getTokens().getLast().getNumber(), "#", 'b');
        thisTokens.getTokens().add(end);

        //System.out.println("符号栈\t状态栈\t下一字符\tACTION");

        for (int i = 0; i < thisTokens.getTokens().size(); ) {
            Token token = thisTokens.getTokens().get(i);    //当前token
            Character thisChar; //下一字符
            if (token.getType() == 'b' || token.getType() == 'l' || token.getType() == 'a') //界符没有单独设置类型
                thisChar = token.getContent().charAt(0);
            else thisChar = token.getType();    //其他符号都是以type区分的

            Integer nowStatus = status.peek();  //当前状态
            String next=gotoTable.getByChar(nowStatus,thisChar);    //下一状态

            StringBuilder stackChar= new StringBuilder();
            StringBuilder stackStatus= new StringBuilder();
            String stackNext="";
            String stackAction="";

            //输出
            for (Character ch : characters) {   //分析栈
                stackChar.append(ch);
            }
            for (Integer integer : status) {    //状态栈
                stackStatus.append(integer).append(",");
            }
            stackNext=thisChar.toString();  //下一字符

            if (next.charAt(0) == 'S') {    //移进
                characters.push(thisChar);  //符号栈压入
                status.push(Integer.valueOf(next.substring(1)));    //状态栈压入
                stackAction="移进";
                lines.add(new ParseResult.Line(stackChar.toString(), stackStatus.toString(),stackNext,stackAction));    //动作

                //新建语法树节点并加入语法森林
                TreeNode newNode;
                if(thisChar=='i') newNode = new TreeNode(token.getContent());   //标识符以原值新建
                else newNode = new TreeNode(thisChar.toString());   //其他都是以类型新建
                treeNodes.add(newNode);

            } else if (next.charAt(0) == 'r') { //规约
                ProcessLine processLine = text.getContent().get(Integer.parseInt(next.substring(1)) - 1);   //根据r后的数字选择产生式规约
                String start = processLine.getLine().split("#")[0].substring(0, 1); //消除语义规则影响
                String production = processLine.getLine().split("#")[0].substring(3);   //消除语义规则影响
                stackAction="规约 "+start+"->"+production;
                lines.add(new ParseResult.Line(stackChar.toString(), stackStatus.toString(),stackNext,stackAction));    //动作


                List<TreeNode> children = new ArrayList<TreeNode>();    //子树
                if (!production.equals("@")) {  //非空字符则按长度弹栈
                    for (int j = production.length() - 1; j >= 0; j--) {
                        status.pop();   //状态栈弹栈
                        Character topChar = characters.pop();   //字符栈弹栈
                        if (production.charAt(j) != topChar) {  //符号栈弹栈不相等则报错
                            throw new TYXException("Input grammar error:Stack error!");
                        }
                    }
                    for (int m = treeNodes.size() - production.length(); m < treeNodes.size(); ) {  //构建子树
                        TreeNode newNode = treeNodes.get(m);
                        treeNodes.remove(m);    //当前森林取出子树
                        children.add(newNode);  //构建子树加入语法树
                    }
                }
                nowStatus = status.peek();  //状态栈弹栈后的下一状态
                status.push(Integer.valueOf(gotoTable.getByInt(nowStatus,(nonTerminals.indexOf(start.charAt(0)) + terminals.size()))));  //转移至下一状态
                Character first = start.charAt(0);  //产生式左部
                characters.push(first); //将产生式左部压入字符栈

                //规约时需要考虑语义分析
                TreeNode newNode = new TreeNode(first.toString(), children);    //构建子树
                if (processLine.getLine().contains("#")) {  //有语义分析的内容
                    StringBuilder quaternion= new StringBuilder();
                    String semantic = processLine.getLine().split("#")[1];  //分离语义分析内容
                    if (semantic.equals("transmit")) {  //值传递 定义为一定是将值从（唯一的）孩子结点传递给父亲结点
                        if (production.contains("(")) newNode.setValue(children.get(1).getValue());
                        else newNode.setValue(children.get(0).getValue());
                    } else if (semantic.contains("real")) { //真值初始化
                        Character ch = semantic.charAt(semantic.indexOf("real") - 2);   //谁的真值
                        Integer integer = Integer.valueOf(ch);
                        children.get(integer - 49).setValue(tokens.getTokens().get(i - 1).getContent());    //找孩子结点位置 0/1/2
                        newNode.setValue(tokens.getTokens().get(i - 1).getContent());   //父亲节点置值
                    } else if (semantic.contains("value") && children.size() >= 3) {    //运算 有临时变量
                        newNode.setValue("T" + countTemp);  //父亲结点设置临时变量
                        countTemp++;    //临时变量++
                    } else if (semantic.contains("assign")) {   //赋值（语法规定为X->X=A;） 赋值语句必输出四元式
                        quaternion.append(":=\t");
                        TreeNode child = children.get(2);   //A
                        if (child.getValue() == null) quaternion.append(child.getName()).append("\t_\t");   //孩子结点没有真值初始化
                        else quaternion.append(child.getValue()).append("\t_\t");   //已经赋了初值
                        quaternion.append(children.get(0).getName());   //父节点X
                    }
                    if (semantic.contains("print")) {   //打印
                        for (TreeNode tn : children) {
                            if (tn.getValue() == null)  quaternion.append(tn.getName()).append("\t");   //符号
                        }
                        for (TreeNode tn : children) {
                            if (tn.getValue() != null)  quaternion.append(tn.getValue()).append("\t");  //两个操作数
                        }
                        if (children.size() < 2) quaternion.append("_\t");  //少了一个操作数
                        quaternion.append(newNode.getValue()).append("\t"); //父节点
                    }
                    newNode.setChildren(children);
                    //quaternion+="\n";
                    if(!quaternion.toString().equals(""))  quaternions.add(quaternion.toString());  //没有需要输出的四元式
                }

                treeNodes.add(newNode); //新子树
                continue;
            } else if (next.equals("acc")) res = true;  //结束了啊
            else {  //异常 “期待的下一字符：”
                for(int ii=0;ii<terminals.size()+nonTerminals.size();ii++){
                    if(!gotoTable.getByInt(nowStatus,ii).equals("n")){
                        if(ii<terminals.size()) information.append(terminals.get(ii)).append(",");
                        else information.append(nonTerminals.get(ii - terminals.size())).append(",");
                    }
                }
                break;  //异常直接退出了
            }
            i++;    //下一token
        }
        return new ParseResult(lines,res, information.toString(),quaternions);
    }
}
