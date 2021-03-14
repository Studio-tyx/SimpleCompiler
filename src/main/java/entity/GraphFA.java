package entity;

import exception.InputException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name GraphFA
 * @description
 * @time
 **/

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

class Vertex {
    char name;
    List<Edge> edges;

    public Vertex(char name, List<Edge> edges) {
        this.name = name;
        this.edges = edges;
    }
}

public class GraphFA {
    private List<Vertex> vertices;
    private Set<Character> terminals;
    private Character finalStatus;

    public GraphFA() {
    }


    public boolean isUpper(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    public boolean isLower(char ch) {
        return ((ch >= 'a' && ch <= 'z') || ch == '@');
    }

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

    public void initRight(Text text) throws InputException {
        vertices = new ArrayList<Vertex>();
        terminals = new HashSet<Character>();
        finalStatus = '$';
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() + ": Grammar has a blank line!");
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
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() + ": Not a format of normal grammar!");
        }
    }

    public void initLeft(Text text) throws InputException {
        vertices = new ArrayList<Vertex>();
        terminals = new HashSet<Character>();
        finalStatus = text.getContent().get(0).getLine().charAt(0);
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() + ": Grammar has a blank line!");
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
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() + ": Not a format of normal grammar!");
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

    //分两遍 第一遍找有的 第二遍找closure
    public Set<Character> findNext(Set<Character> I, char weight) {
        return closure(move(I, weight));
    }

    //如果ch==顶点 把所有权重与weight相等的点加入res
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

    public void showNFA() {
        System.out.println("-------------NFA--------------");
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.edges) {
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
        System.out.println("--NFA terminals----------------------------");
        for (Character ch : terminals) {
            System.out.print(ch + ",");
        }
        System.out.println("\n------------------------------");
    }

    public <T> void show(Set<T> set) {
        System.out.println("--set------");
        for (T t : set) {
            System.out.print(t + ",");
        }
        System.out.println("\n--------------");
    }
}
