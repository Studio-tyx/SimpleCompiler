package entity;

public interface Graph<E> {
    void addEdge(E thisVertex, E nextVertex, E weight);
    E getFirst();
    E nextStatus(E thisStatus,E weight);
    void show();
}
