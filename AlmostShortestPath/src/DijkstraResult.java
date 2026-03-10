import java.util.ArrayList;

public class DijkstraResult {
    int[] dist;
    ArrayList<Edge>[] prevEdges;

    DijkstraResult(int[] dist, ArrayList<Edge>[] prevEdges) {
        this.dist = dist;
        this.prevEdges = prevEdges;
    }
}