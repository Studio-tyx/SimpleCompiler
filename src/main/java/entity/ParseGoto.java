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
    private List<TreeNode> treeNodes;

    /**
     * 成员变量初始化 由语法分析终结符与非终结符
     *
     * @param text 语法读入
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
     * @param parseDFA 输入的DFA
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
     * @param tokens token集合
     * @return 是否符合语法
     */
    public ParseResult check(Tokens tokens) throws TYXException {
        Tokens thisTokens = tokens; //把形参变成实参 主要是也没啥必要写一遍setTokens
        boolean res = false;    //结果
        Stack<Integer> status = new Stack<Integer>();   //状态栈
        Stack<Character> characters = new Stack<Character>();   //符号栈
        treeNodes = new ArrayList<TreeNode>();   //现有的语法树（或语法子树）
        int countTemp = 0;  //临时变量计数器
        List<ParseResult.Line> lines=new ArrayList<ParseResult.Line>();
        List<String> quaternions=new ArrayList<String>();
        List<Character> terminals= gotoTable.getTerminals();  //终结符集合
        List<Character> nonTerminals= getGotoTable().getNonTerminals();   //非终结符集合
        String information="";

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
            String next=gotoTable.getByChar(nowStatus,thisChar);

            String stackChar="",stackStatus="",stackNext="",stackAction="";
            //输出
            for (Character ch : characters) {
                stackChar+=ch;
            }
            for (Integer integer : status) {
                stackStatus+=(integer+",");
            }
            stackNext=thisChar.toString();
            stackAction=next;
            lines.add(new ParseResult.Line(stackChar,stackStatus,stackNext,stackAction));

            if (next.charAt(0) == 'S') {    //移进
                characters.push(thisChar);  //符号栈压入
                status.push(Integer.valueOf(next.substring(1)));    //状态栈压入

                //新建语法树节点并加入语法森林
                TreeNode newNode;
                if(thisChar=='i'){
                    newNode = new TreeNode(token.getContent());
                }
                else {
                    newNode = new TreeNode(thisChar.toString());
                }
                treeNodes.add(newNode);
            } else if (next.charAt(0) == 'r') { //规约
                ProcessLine processLine = text.getContent().get(Integer.parseInt(next.substring(1)) - 1);   //根据r后的数字选择产生式规约
                String start = processLine.getLine().split("#")[0].substring(0, 1);
                String production = processLine.getLine().split("#")[0].substring(3);

                List<TreeNode> children = new ArrayList<TreeNode>();    //子树
                if (!production.equals("@")) {  //非空字符则按长度弹栈
                    for (int j = production.length() - 1; j >= 0; j--) {
                        status.pop();   //状态栈弹栈
                        Character topChar = characters.pop();
                        if (production.charAt(j) != topChar) {  //符号栈弹栈不相等则报错
                            throw new TYXException("Input grammar error:Stack error!");
                        }
                    }
                    for (int m = treeNodes.size() - production.length(); m < treeNodes.size(); ) {  //构建子树
                        TreeNode newNode = treeNodes.get(m);
                        treeNodes.remove(m);
                        children.add(newNode);
                    }
                }
                nowStatus = status.peek();  //状态栈弹栈后的下一状态
//                status.push(Integer.valueOf(Goto[nowStatus][(nonTerminals.indexOf(start.charAt(0)) + terminals.size())]));  //转移至下一状态
                status.push(Integer.valueOf(gotoTable.getByInt(nowStatus,(nonTerminals.indexOf(start.charAt(0)) + terminals.size()))));  //转移至下一状态
                Character first = start.charAt(0);  //产生式左部
                characters.push(first); //压入产生式左部

                TreeNode newNode = new TreeNode(first.toString(), children);    //构建子树
                if (processLine.getLine().contains("#")) {  //有语义分析的内容
                    String quaternion="";
                    String semantic = processLine.getLine().split("#")[1];  //分离语义分析内容
                    if (semantic.equals("transmit")) {  //值传递
                        if (production.contains("(")) newNode.setValue(children.get(1).getValue());
                        else newNode.setValue(children.get(0).getValue());
                    } else if (semantic.contains("real")) { //真值
                        Character ch = semantic.charAt(semantic.indexOf("real") - 2);
                        Integer integer = Integer.valueOf(ch);
                        children.get(integer - 49).setValue(tokens.getTokens().get(i - 1).getContent());
                        newNode.setValue(tokens.getTokens().get(i - 1).getContent());
                    } else if (semantic.contains("value") && children.size() >= 3) {
                        newNode.setValue("T" + countTemp);
                        countTemp++;
                    } else if (semantic.contains("assign")) {   //赋值
                        quaternion+=":=\t";
                        TreeNode child = children.get(2);
                        if (child.getValue() == null) quaternion+=(child.getName() + "\t_\t");
                        else quaternion+=(child.getValue() + "\t_\t");
                        quaternion+=(children.get(0).getName());

                    } else {
                    }
                    if (semantic.contains("print")) {   //打印
                        if (semantic.contains("=") || production.contains(";")) quaternion+=":=\t";
                        for (TreeNode tn : children) {
                            if (tn.getValue() == null) {
                                if (!tn.getName().equals("=")) quaternion+=(tn.getName() + "\t");
                            }
                        }
                        for (TreeNode tn : children) {
                            if (tn.getValue() != null && !tn.getName().equals("="))
                                quaternion+=(tn.getValue() + "\t");
                        }
                        if (children.size() < 2) quaternion+=("_\t");
                        quaternion+=(newNode.getValue() + "\t");
                    }
                    newNode.setChildren(children);
                    //quaternion+="\n";
                    if(quaternion!="")  quaternions.add(quaternion);
                }
                treeNodes.add(newNode);
                continue;
            } else if (next.equals("acc")) res = true;  //结束了啊
            else {  //基本是出错的
                for(int ii=0;ii<terminals.size()+nonTerminals.size();ii++){
                    if(!gotoTable.getByInt(nowStatus,ii).equals("n")){
                        if(ii<terminals.size())information+=(terminals.get(ii)+",");
                        else information+=(nonTerminals.get(ii-terminals.size())+",");
                    }
                }
                break;
            }
            i++;    //下一token
        }
        ParseResult result=new ParseResult(lines,res,information,quaternions);
        return result;
    }
}
