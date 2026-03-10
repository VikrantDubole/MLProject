import java.util.*;

public class Main {
    static final int INF = 1_000_000_000;

    static DijkstraResult dijkstra(ArrayList<Edge>[] graph, int source) {
        int n = graph.length;

        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;

        @SuppressWarnings("unchecked")
        ArrayList<Edge>[] prevEdges = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            prevEdges[i] = new ArrayList<>();
        }

        MinHeap pq = new MinHeap(n);

        for (int v = 0; v < n; v++) {
            pq.Insert(v, dist[v]);
        }

        while (!pq.isEmpty()) {
            HeapNode current = pq.ExtractMin();
            int u = current.item;

            if (current.priority == INF) {
                break;
            }

            for (Edge e : graph[u]) {
                if (e.removed)
                    continue;

                int v = e.to;

                if (!pq.contains(v))
                    continue;

                int alt = dist[u] + e.weight;

                if (alt < dist[v]) {
                    dist[v] = alt;
                    prevEdges[v].clear();
                    prevEdges[v].add(e);
                    pq.ChangePriority(v, alt);
                } else if (alt == dist[v]) {
                    prevEdges[v].add(e);
                }
            }
        }

        return new DijkstraResult(dist, prevEdges);
    }

    static void removeCriticalEdges(ArrayList<Edge>[] prevEdges, int target) {
        int n = prevEdges.length;
        boolean[] visited = new boolean[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        queue.offer(target);
        visited[target] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (Edge e : prevEdges[current]) {
                e.removed = true;
                int parent = e.from;

                if (!visited[parent]) {
                    visited[parent] = true;
                    queue.offer(parent);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        while (true) {
            int n = fs.nextInt();
            if (n == Integer.MIN_VALUE)
                break;

            int m = fs.nextInt();
            if (n == 0 && m == 0)
                break;

            int source = fs.nextInt();
            int target = fs.nextInt();

            @SuppressWarnings("unchecked")
            ArrayList<Edge>[] graph = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                graph[i] = new ArrayList<>();
            }

            for (int i = 0; i < m; i++) {
                int u = fs.nextInt();
                int v = fs.nextInt();
                int p = fs.nextInt();
                graph[u].add(new Edge(u, v, p));
            }

            DijkstraResult first = dijkstra(graph, source);

            if (first.dist[target] == INF) {
                out.append(-1).append('\n');
                continue;
            }

            removeCriticalEdges(first.prevEdges, target);

            DijkstraResult second = dijkstra(graph, source);

            if (second.dist[target] == INF) {
                out.append(-1).append('\n');
            } else {
                out.append(second.dist[target]).append('\n');
            }
        }

        System.out.print(out.toString());
    }
}