package entity;

import exception.InputException;
import tool.CharacterTools;
import tool.ShowTools;

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
    private String[][] Goto;    //Goto表
    private List<Character> terminals;  //终结符集合
    private List<Character> nonTerminals;   //非终结符集合
    private int statusNumber;   //状态总数
    private Text text;  //语法

    /**
     * 成员变量初始化 由语法分析终结符与非终结符
     *
     * @param text 语法读入
     */
    public void init(Text text) {
        this.text = text;
        terminals = new ArrayList<Character>();
        nonTerminals = new ArrayList<Character>();

        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            for (int i = 0; i < line.length(); i++) {
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
    }

    /**
     * 由DFA转换为Goto表
     *
     * @param parseDFA 输入的DFA
     * @throws InputException 非LR(1)文法报错
     */
    public void createGoto(ParseDFA parseDFA) throws InputException {
        statusNumber = parseDFA.getStatus().size();
        Goto = new String[statusNumber][terminals.size() + nonTerminals.size()];
        LinkGraph<Set<LRLine>, Character> graph = parseDFA.getGraph();

        for (int i = 0; i < statusNumber; i++) {
            for (int j = 0; j < terminals.size() + nonTerminals.size(); j++) {  //每行表格初始化（便于后续判断是否为LR1文法）
                Goto[i][j] = "n";
            }
            Set<LRLine> thisSet = parseDFA.getStatus().get(i);  //当前状态
            for (LRLine lrLine : thisSet) {
                if (lrLine.getStart() == '%' && lrLine.getContent().equals("S·")) { //设置%->S·为acc
                    if (!Goto[i][terminals.size() - 1].equals("n")) //重复写 说明非LR1文法
                        throw new InputException("Input grammar error:Not a LR(1) grammar!");
                    else Goto[i][terminals.size() - 1] = "acc";
                }
                if (parseDFA.endProduction(lrLine.getContent())) { //判断是否有到底的句子->r规约
                    for (Character forwardSearch : lrLine.getForwardSearch()) {
                        if (lrLine.getProductionNumber() != 0) {    //'S->S 第0号产生式不算
                            if (!Goto[i][terminals.indexOf(forwardSearch)].equals("n")) //重复写 说明非LR1文法
                                throw new InputException("Input grammar error:Not a LR(1) grammar!");
                            else Goto[i][terminals.indexOf(forwardSearch)] = "r" + lrLine.getProductionNumber();
                        }
                    }
                }
            }
            for (int j = 0; j < terminals.size(); j++) {    //对于每个终结符 判断是否可以到达新的状态
                Set<LRLine> nextSet = graph.findNextVertex(thisSet, terminals.get(j));
                if (nextSet != null) {
                    if (!Goto[i][j].equals("n")) //重复写 说明非LR1文法
                        throw new InputException("Input grammar error:Not a LR(1) grammar!");
                    else Goto[i][j] = "S" + parseDFA.getIndex(parseDFA.getStatus(), nextSet);
                }
            }
            for (int j = 0; j < nonTerminals.size(); j++) {    //对于每个非终结符 判断下一个状态
                Set<LRLine> nextSet = graph.findNextVertex(thisSet, nonTerminals.get(j));
                if (nextSet != null) {
                    if (!Goto[i][j + terminals.size()].equals("n")) //重复写 说明非LR1文法
                        throw new InputException("Input grammar error:Not a LR(1) grammar!");
                    else
                        Goto[i][j + terminals.size()] = String.valueOf(parseDFA.getIndex(parseDFA.getStatus(), nextSet));
                }
            }
        }
        System.out.println("Action表:");
        for (Character ch : terminals) System.out.print(ch + "\t");
        for (Character ch : nonTerminals) System.out.print(ch + "\t");
        System.out.println();
        ShowTools.show(Goto);
        System.out.println();
    }

    /**
     * 根据tokens判断是否符合语法
     *
     * @param tokens token集合
     * @return 是否符合语法
     */
    public boolean check(Tokens tokens) {
        Tokens thisTokens = tokens; //把形参变成实参 主要是也没啥必要写一遍setTokens
        boolean res = false;
        Stack<Integer> status = new Stack<Integer>();
        Stack<Character> characters = new Stack<Character>();

        //初态
        status.push(0);
        characters.push('#');

        //tokens尾部加#便于判断是否结束
        Token end = new Token(thisTokens.getTokens().getLast().getNumber(), "#", 'b');
        thisTokens.getTokens().add(end);

        System.out.println("符号栈\t状态栈\t下一字符\tACTION");

        for (int i = 0; i < thisTokens.getTokens().size(); ) {
            Token token = thisTokens.getTokens().get(i);
            Character thisChar;
            if (token.getType() == 'b') thisChar = token.getContent().charAt(0);    //界符没有单独设置类型
            else thisChar = token.getType();    //其他符号都是以type区分的

            Integer nowStatus = status.peek();
            String next = Goto[nowStatus][CharacterTools.isUpper(thisChar) ? nonTerminals.indexOf(thisChar) + terminals.size() : terminals.indexOf(thisChar)];  //下一状态

            //输出
            for (Character ch : characters) {
                System.out.print(ch);
            }
            System.out.print("\t\t");
            for (Integer integer : status) {
                System.out.print(integer + ",");
            }
            System.out.println("\t\t" + thisChar + "\t\t" + next);

            if (next.charAt(0) == 'S') {    //移进
                characters.push(thisChar);
                status.push(Integer.valueOf(next.substring(1)));
            } else if (next.charAt(0) == 'r') { //规约
                ProcessLine processLine = text.getContent().get(Integer.parseInt(next.substring(1)) - 1);   //根据r后的数字选择产生式规约
                String start = processLine.getLine().substring(0, 1);
                String production = processLine.getLine().substring(3);
                if (!production.equals("@")) {  //非空字符则按长度弹栈
                    for (int j = production.length() - 1; j >= 0; j--) {
                        status.pop();   //状态栈弹栈
                        if (production.charAt(j) != characters.pop()) {  //比较符号是否相等
                            System.out.println("error");
                            System.out.println(production);
                            System.out.println(characters);
                        }
                    }
                }
                nowStatus = status.peek();
                status.push(Integer.valueOf(Goto[nowStatus][(nonTerminals.indexOf(start.charAt(0)) + terminals.size())]));  //转移至下一状态
                characters.push(start.charAt(0));   //压入产生式左部
                continue;
            } else if (next.equals("acc")) res = true;  //结束了啊
            else {  //基本是出错的
                System.out.println(next);
            }
            i++;    //下一token
        }
        return res;
    }
}
