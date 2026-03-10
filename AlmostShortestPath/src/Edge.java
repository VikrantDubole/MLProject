public class Edge {
    int from;
    int to;
    int weight;
    boolean removed;

    Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.removed = false;
    }
}