package entity;

import java.util.ArrayList;
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

public class ListGraph implements Graph<Character> {
    //默认第一个点是初态
    private final List<Vertex> vertices;

    public ListGraph() {
        vertices = new ArrayList<Vertex>();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Character getFirst(){return vertices.get(0).name;}

    /*
    只有右线性的增加 左线性没写
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

    public Character nextStatus(Character thisStatus,Character weight){
        Character res=null;
        for(Vertex vertex:vertices){
            if(vertex.name==thisStatus){
                for(Edge edge:vertex.edges){
                    if(weight==edge.weight){
                        res=edge.nextVertex;
                    }
                }
            }
        }
        return res;
    }

    public void show(Set<Character> I) {
        for (char ch : I) {
            System.out.print(ch + ",");
        }
        System.out.println();
    }


    public void show() {
        System.out.println("ListGraph show:");
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.edges) {
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
    }


}
