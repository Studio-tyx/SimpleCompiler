package entity;

import exception.InputException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
GraphFA与MatrixFA没有做成FA子类的原因：
GraphFA的状态类型为character（代码读入为character）
MatrixFA的状态类型为integer（防止character状态类型不够用）
两个类型不一 做成同一个类的子类有一点牵强

其实本来想把GraphFA和MatrixFA单独做成两个泛型的
但是在这次词法分析器中已经固定了某些类型 例如右线性文法中终态为$（character）
不适合做成泛型
 */

/**
 * @author TYX
 * @name GraphFA
 * @description NFA（用邻接表实现了图结构，状态类型均为character）
 * @time  2021/3/14 14:17
 **/

/**
 * 边
 */
class Edge {
    char nextVertex;
    char weight;

    public Edge() {
    }

    public Edge(char nextVertex, char weight) {
        this.weight = weight;
        this.nextVertex = nextVertex;
    }
}

/**
 * 顶点
 */
class Vertex {
    char name;
    List<Edge> edges;

    public Vertex(char name, List<Edge> edges) {
        this.name = name;
        this.edges = edges;
    }
}

public class GraphFA {
    private List<Vertex> vertices;  //图结构
    private Set<Character> terminals;   //终结符集合
    private Character finalStatus;  //终态集合

    public GraphFA() {
    }

    /**
     * 是否为大写字符
     *
     * @param ch 需要判断的字符
     * @return 是否为大写
     */
    public boolean isUpper(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    /**
     * 是否为小写字符
     *
     * @param ch 需要判断的字符
     * @return 是否为小写
     */
    public boolean isLower(char ch) {
        return ((ch >= 'a' && ch <= 'z') || ch == '@');
    }

    /**
     * 根据读入的文本生成NFA（根据文本结构判断是左线性还是右线性）
     *
     * @param text 输入的文本
     * @throws InputException 输入格式异常等
     */
    public void init(Text text) throws InputException {
        boolean isLeft = true, isRight = true;
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.length() == 4) {

            } else if (line.length() == 5) {
                if (isLower(line.charAt(3))) {
                    //isRight=true;
                    isLeft = false;
                } else {
                    isRight = false;
                    //isLeft=true;
                }
            }
            if (!isLeft && !isRight)
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() +
                        ":Grammar format error! Please select left linear grammar or right linear grammar!");
        }
        if (isRight) initRight(text);
        else if (isLeft) initLeft(text);
        showNFA();
    }

    /**
     * 右线性文法的初始化
     *
     * @param text 输入文本
     * @throws InputException 输入异常
     */
    public void initRight(Text text) throws InputException {
        vertices = new ArrayList<Vertex>();
        terminals = new HashSet<Character>();
        finalStatus = '$';
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new InputException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Grammar has a blank line!");
                //break;
            }
            boolean isLegal = false;
            if (line.contains("->")) {
                String[] afterSplit = line.split("->");
                String former = afterSplit[0], latter = afterSplit[1];
                if (former.length() == 1) {
                    if (isUpper(former.charAt(0))) {
                        if (latter.length() == 1) {
                            if (isLower(latter.charAt(0))) {
                                isLegal = true;
                                addEdge(former.charAt(0), '$', latter.charAt(0));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(0));
                            }
                        } else if (latter.length() == 2) {
                            if (isLower(latter.charAt(0)) && isUpper(latter.charAt(1))) {
                                isLegal = true;
                                addEdge(former.charAt(0), latter.charAt(1), latter.charAt(0));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(0));
                            }
                        }
                    }
                }
            }
            if (!isLegal)
                throw new InputException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Not a format of normal grammar!");
        }
    }

    /**
     * 左线性文法的初始化
     *
     * @param text 输入文本
     * @throws InputException 输入格式初始化
     */
    public void initLeft(Text text) throws InputException {
        vertices = new ArrayList<Vertex>();
        terminals = new HashSet<Character>();
        finalStatus = text.getContent().get(0).getLine().charAt(0);
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new InputException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Grammar has a blank line!");
                //break;
            }
            boolean isLegal = false;
            if (line.contains("->")) {
                String[] afterSplit = line.split("->");
                String former = afterSplit[0], latter = afterSplit[1];
                if (former.length() == 1) {
                    if (isUpper(former.charAt(0))) {
                        if (latter.length() == 1) {
                            if (isLower(latter.charAt(0))) {
                                isLegal = true;
                                addEdge('$', former.charAt(0), latter.charAt(0));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(0));
                            }
                        } else if (latter.length() == 2) {
                            if (isUpper(latter.charAt(0)) && isLower(latter.charAt(1))) {
                                isLegal = true;
                                addEdge(latter.charAt(0), former.charAt(0), latter.charAt(1));
                                if (latter.charAt(0) != '@') terminals.add(latter.charAt(1));
                            }
                        }
                    }
                }
            }
            if (!isLegal)
                throw new InputException("Input grammar error at line " + processLine.getLineNumber()
                        + ": Not a format of normal grammar!");
        }
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Set<Character> getTerminals() {
        return terminals;
    }

    public Character getFinalStatus() {
        return finalStatus;
    }

    /**
     * NFA->DFA 根据读入的终结符寻找下一个状态
     *
     * @param I 当前状态集
     * @param weight 输入的终结符
     * @return 下一个状态的集合
     */
    public Set<Character> findNext(Set<Character> I, char weight) {
        return closure(move(I, weight));
    }

    /**
     * 将终结符边加入到状态集
     *
     * @param I 当前状态集
     * @param weight 读入的终结符
     * @return 加入下一条边之后到达的状态
     */
    public Set<Character> move(Set<Character> I, char weight) {
        Set<Character> res = new HashSet<Character>();
        for (char ch : I) {
            for (Vertex vertex : vertices) {
                if (ch == vertex.name) {
                    for (Edge edge : vertex.edges) {
                        if (edge.weight == weight) {
                            res.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * epsilon-closure 寻找当前状态的闭包
     *
     * @param I 当前状态
     * @return 状态的闭包（加入epsilon边可以到达的状态集）
     */
    public Set<Character> closure(Set<Character> I) {
        for (char ch : I) {
            for (Vertex vertex : vertices) {
                if (ch == vertex.name) {
                    for (Edge edge : vertex.edges) {
                        if (edge.weight == '@') {
                            I.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return I;
    }

    /**
     * 图的构造（新增一条边）
     *
     * @param thisVertex 当前结点
     * @param nextVertex 下一结点
     * @param weight 边的权重
     */
    public void addEdge(Character thisVertex, Character nextVertex, Character weight) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            if (vertex.name == thisVertex) {//找到点了
                for (Edge edge : vertex.edges) {
                    if (edge.nextVertex == nextVertex && edge.weight == weight) return;//完全一样就返回
                }
                //加边
                Edge newEdge = new Edge(nextVertex, weight);
                vertex.edges.add(newEdge);
                vertices.set(i, vertex);
                return;
            }
        }
        Edge newEdge = new Edge(nextVertex, weight);
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(newEdge);
        Vertex newVertex = new Vertex(thisVertex, edges);
        vertices.add(newVertex);
        return;
    }

    /**
     * NFA图结构的展示
     */
    public void showNFA() {
        System.out.println("-------------NFA--------------");
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.edges) {
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
//        System.out.println("--NFA terminals----------------------------");
//        for (Character ch : terminals) {
//            System.out.print(ch + ",");
//        }
        System.out.println("\n------------------------------");
    }

    /**
     * 集合展示 便于输出
     *
     * @param set 集合
     * @param <T> 类型自定
     */
    public <T> void show(Set<T> set) {
        System.out.println("--set------");
        for (T t : set) {
            System.out.print(t + ",");
        }
        System.out.println("\n--------------");
    }
}
