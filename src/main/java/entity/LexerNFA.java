package entity;

import exception.TYXException;
import tool.CharacterTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name LexerNFA
 * @description NFA（用邻接表实现了图结构，状态类型均为character）
 * @createTime 2021/3/14 14:17
 **/


public class LexerNFA {
    private LinkGraph<Character, Character> graph;
    private Set<Character> terminals;   //终结符集合
    private Character finalStatus;  //终态集合

    public LexerNFA() {
    }


    public Character getFirstElement() {
        return graph.getVertices().get(0).name;
    }

    public Set<Character> getTerminals() {
        return terminals;
    }

    public Character getFinalStatus() {
        return finalStatus;
    }

    public LinkGraph<Character, Character> getGraph() {
        return graph;
    }

    /**
     * 根据读入的文本生成NFA（根据文本结构判断是左线性还是右线性）
     *
     * @param text 输入的文本 Text
     * @return NFA展示String List of String
     * @throws TYXException 输入格式异常等
     */
    public List<String> init(Text text) throws TYXException {
        boolean isLeft = true, isRight = true;
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.length() == 4) {   //A->a

            } else if (line.length() == 5) {
                if (CharacterTools.isLower(line.charAt(3)) || CharacterTools.isAt(line.charAt(3))) {    //我定义了A->@B的词法
                    isLeft = false;
                } else {
                    isRight = false;
                }
            }
            if (!isLeft && !isRight)
                throw new TYXException("Input grammar error at line " + processLine.getLineNumber() +
                        ":Grammar format error! Please select left linear grammar or right linear grammar!");
        }
        if (isRight) initRight(text);
        else if (isLeft) initLeft(text);
        //showNFA();
        return getNFA();
    }

    /**
     * 右线性文法的初始化
     *
     * @param text 输入文本 Text
     * @throws TYXException 输入异常
     */
    public void initRight(Text text) throws TYXException {
        graph = new LinkGraph<Character, Character>();
        //List<Vertex<Character>> vertices=graph.getVertices();
        terminals = new HashSet<Character>();
        finalStatus = '$';
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new TYXException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Grammar has a blank line!");
            }
            boolean isLegal = false;
            if (line.contains("->")) {
                String[] afterSplit = line.split("->");
                String former = afterSplit[0], latter = afterSplit[1];  //分离
                if (former.length() == 1) {
                    if (CharacterTools.isUpper(former.charAt(0))) {
                        if (latter.length() == 1) { //A->a
                            if (CharacterTools.isLower(latter.charAt(0)) || CharacterTools.isAt(latter.charAt(0))) {
                                isLegal = true;
                                graph.addEdge(former.charAt(0), '$', latter.charAt(0)); //$是终态
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(0));
                            }
                        } else if (latter.length() == 2) {  //A->aB
                            if ((CharacterTools.isLower(latter.charAt(0)) || CharacterTools.isAt(latter.charAt(0))) && CharacterTools.isUpper(latter.charAt(1))) {
                                isLegal = true;
                                graph.addEdge(former.charAt(0), latter.charAt(1), latter.charAt(0));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(0));
                            }
                        }
                    }
                }
            }
            if (!isLegal)
                throw new TYXException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Not a format of normal grammar!");
        }
    }

    /**
     * 左线性文法的初始化
     *
     * @param text 输入文本 Text
     * @throws TYXException 输入格式初始化
     */
    public void initLeft(Text text) throws TYXException {
        graph = new LinkGraph<Character, Character>();
        terminals = new HashSet<Character>();
        finalStatus = text.getContent().get(0).getLine().charAt(0);
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new TYXException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Grammar has a blank line!");
                //break;
            }
            boolean isLegal = false;
            if (line.contains("->")) {
                String[] afterSplit = line.split("->");
                String former = afterSplit[0], latter = afterSplit[1];
                if (former.length() == 1) {
                    if (CharacterTools.isUpper(former.charAt(0))) {
                        if (latter.length() == 1) {
                            if (CharacterTools.isLower(latter.charAt(0)) || CharacterTools.isAt(latter.charAt(0))) {
                                isLegal = true;
                                graph.addEdge('$', former.charAt(0), latter.charAt(0));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(0));
                            }
                        } else if (latter.length() == 2) {
                            if (CharacterTools.isUpper(latter.charAt(0)) && (CharacterTools.isLower(latter.charAt(1)) || CharacterTools.isAt(latter.charAt(1)))) {
                                isLegal = true;
                                graph.addEdge(latter.charAt(0), former.charAt(0), latter.charAt(1));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(1));
                            }
                        }
                    }
                }
            }
            if (!isLegal)
                throw new TYXException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Not a format of normal grammar!");
        }
    }

    /**
     * NFA-》DFA<br>
     *     根据读入的终结符寻找下一个状态
     *
     * @param I      当前状态集 Set of Character
     * @param weight 输入的终结符 Set of Character
     * @return 下一个状态的集合 Set of Character
     */
    public Set<Character> findNext(Set<Character> I, Character weight) {
        return closure(move(I, weight));
    }

    /**
     * 将终结符边加入到状态集
     *
     * @param I      当前状态集 Set of Character
     * @param weight 读入的终结符 Character
     * @return 加入下一条边之后到达的状态 Set of Character
     */
    public Set<Character> move(Set<Character> I, Character weight) {
        Set<Character> res = new HashSet<Character>();
        for (Character ch : I) {
            for (Vertex<Character, Character> vertex : graph.getVertices()) {
                if (ch.equals(vertex.name)) {
                    for (Edge<Character, Character> edge : vertex.edges) {
                        if (edge.weight.equals(weight)) {
                            res.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * epsilon-closure<br>
     *     寻找当前状态的闭包
     *
     * @param I 当前状态 Set of Character
     * @return 状态的闭包（加入epsilon边可以到达的状态集） Set of Character
     */
    public Set<Character> closure(Set<Character> I) {
        for (Character ch : I) {
            for (Vertex<Character, Character> vertex : graph.getVertices()) {
                if (ch.equals(vertex.name)) {
                    for (Edge<Character, Character> edge : vertex.edges) {
                        if (edge.weight.equals('@')) {
                            I.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return I;
    }

    /**
     * NFA图结构的展示
     */
    public void showNFA() {
        System.out.println("-------------NFA--------------");
        for (Vertex<Character, Character> vertex : graph.getVertices()) {
            for (Edge<Character, Character> edge : vertex.edges) {
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
        System.out.println("\n------------------------------");
    }

    /**
     * NFA图结构的展示
     *
     * @return NFA展示String（Frame展示用） List of String
     */
    public List<String> getNFA() {
        List<String> res=new ArrayList<String>();
        for (Vertex<Character, Character> vertex : graph.getVertices()) {
            for (Edge<Character, Character> edge : vertex.edges) {
                res.add(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
        return res;
    }

    /**
     * 集合展示 便于输出
     *
     * @param set 集合 Set of T
     * @param <T> 类型自定 T
     */
    public <T> void show(Set<T> set) {
        System.out.println("--set------");
        for (T t : set) {
            System.out.print(t + ",");
        }
        System.out.println("\n--------------");
    }
}
