package entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name ListGraph
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


public class ListGraph {
    //默认第一个点是初态
    private final List<Vertex> vertices;

    public ListGraph() {
        vertices = new ArrayList<Vertex>();
    }


    /*
    只有右线性的增加 左线性没写
     */
    public void addEdge(char thisVertex, char nextVertex, char weight) {
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

    //分两遍 第一遍找有的 第二遍找closure
    public Set<Character> findNext(Set<Character> I, char weight) {
        return closure(move(I, weight));
    }

    public void show(Set<Character> I) {
        for (char ch : I) {
            System.out.print(ch + ",");
        }
        System.out.println();
    }

    public void show() {
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.edges) {
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
    }


}
