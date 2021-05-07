package entity;

/**
 * @author TYX
 * @name LinkGraph
 * @description 邻接表实现的图结构（泛型）
 * @createTime 2021/3/19 9:29
 **/

import java.util.ArrayList;
import java.util.List;

/**
 * 边结构
 *
 * @param <T> 顶点类型
 * @param <W> 权重类型
 */
class Edge<T, W> {
    T nextVertex;
    W weight;

    /**
     * 构造器
     */
    public Edge() {
    }

    /**
     * 构造器
     *
     * @param nextVertex 顶点类型 T
     * @param weight 权重类型 W
     */
    public Edge(T nextVertex, W weight) {
        this.weight = weight;
        this.nextVertex = nextVertex;
    }
}

/**
 * 顶点结构
 *
 * @param <T> 顶点类型
 * @param <W> 权重类型
 */
class Vertex<T, W> {
    T name;
    List<Edge<T, W>> edges;

    /**
     * 构造器
     *
     * @param name 顶点类型 T
     * @param edges 权重类型 W
     */
    public Vertex(T name, List<Edge<T, W>> edges) {
        this.name = name;
        this.edges = edges;
    }
}

/**
 * 图结构
 *
 * @param <T> 顶点类型
 * @param <W> 权重类型
 */
public class LinkGraph<T, W> {
    private final List<Vertex<T, W>> vertices;  //图结构

    /**
     * 构造器
     */
    public LinkGraph() {
        vertices = new ArrayList<Vertex<T, W>>();
    }

    /**
     * 返回图结构
     *
     * @return 图结构 List of Vertex
     */
    public List<Vertex<T, W>> getVertices() {
        return vertices;
    }

    /**
     * 图的构造（新增一条边）<br>
     * 重复的点-点 权值相等也是不加的
     *
     * @param thisVertex 当前结点 T
     * @param nextVertex 下一结点 T
     * @param weight     边的权重 W
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
    }

    /**
     * 根据当前顶点和边找下一顶点
     *
     * @param thisVertex 当前顶点 T
     * @param weight 边（权重） W
     * @return 下一顶点 T
     */
    public T findNextVertex(T thisVertex, W weight) {
        for (Vertex<T, W> vertex : vertices) {
            if (vertex.name.equals(thisVertex)) {
                for (Edge<T, W> edge : vertex.edges) {
                    if (weight.equals(edge.weight)) return edge.nextVertex;
                }
            }
        }
        return null;
    }

    /**
     * 图中是否存在某个元素（自行构造的数据类型需要额外定义equals）
     *
     * @param thisVertex 某个元素 T
     * @return 是否存在 boolean
     */
    public boolean exist(T thisVertex) {
        for (Vertex<T, W> vertex : vertices) {
            if (vertex.name.equals(thisVertex)) return true;
            for (Edge<T, W> edge : vertex.edges) {
                if (edge.nextVertex.equals(thisVertex)) return true;
            }
        }
        return false;
    }

    /**
     * 图结构的输出
     */
    public void show() {
        for (Vertex<T, W> vertex : vertices) {
            for (Edge<T, W> edge : vertex.edges) {
//                System.out.println();
//                System.out.println(vertex.name);
//                System.out.println("--------------" + edge.weight + "-------------");
//                System.out.println(edge.nextVertex);
//                System.out.println();
                System.out.println(vertex.name + "->" + edge.nextVertex + ":" + edge.weight);
            }
        }
    }

}
