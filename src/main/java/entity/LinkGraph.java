package entity;

/**
 * @author TYX
 * @name LinkGraph
 * @description
 * @time 2021/3/19 9:29
 **/

import java.util.ArrayList;
import java.util.List;

/**
 * 边
 */
class Edge<T, W> {
    T nextVertex;
    W weight;

    public Edge() {
    }

    public Edge(T nextVertex, W weight) {
        this.weight = weight;
        this.nextVertex = nextVertex;
    }
}

/**
 * 顶点
 */
class Vertex<T, W> {
    T name;
    List<Edge<T, W>> edges;

    public Vertex(T name, List<Edge<T, W>> edges) {
        this.name = name;
        this.edges = edges;
    }
}

public class LinkGraph<T, W> {
    private final List<Vertex<T, W>> vertices;  //图结构

    public LinkGraph() {
        vertices = new ArrayList<Vertex<T, W>>();
    }

    public List<Vertex<T, W>> getVertices() {
        return vertices;
    }

    /**
     * 图的构造（新增一条边）
     * 重复的点-点 权值相等也是不加的
     *
     * @param thisVertex 当前结点
     * @param nextVertex 下一结点
     * @param weight     边的权重
     */
    public void addEdge(T thisVertex, T nextVertex, W weight) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex<T, W> vertex = vertices.get(i);
            if (vertex.name.equals(thisVertex)) {//找到点了
                for (Edge<T, W> edge : vertex.edges) {
                    if (edge.nextVertex.equals(nextVertex) && edge.weight.equals(weight)) return;//完全一样就返回
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

    public T findNextVertex(T thisVertex, W weight){
        for(Vertex<T,W> vertex:vertices){
            if(vertex.name.equals(thisVertex)){
                for(Edge<T,W> edge:vertex.edges){
                    if(weight.equals(edge.weight))  return edge.nextVertex;
                }
            }
        }
        return null;
    }

    public boolean exist(T thisVertex) {
        for (Vertex<T, W> vertex : vertices) {
            if (vertex.name.equals(thisVertex)) return true;
            for (Edge<T, W> edge : vertex.edges) {
                if (edge.nextVertex.equals(thisVertex)) return true;
            }
        }
        return false;
    }

    public void show() {
        for (Vertex<T, W> vertex : vertices) {
            for (Edge<T, W> edge : vertex.edges) {
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
    }

}
