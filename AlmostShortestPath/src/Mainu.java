package AlmostShortestPath.src;

import java.io.*;
import java.util.*;

public class Mainu {
    static final int INF = 1_000_000_000;

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = read();
            } while (c <= ' ' && c != -1);

            if (c == -1)
                return Integer.MIN_VALUE;

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }

            int value = 0;
            while (c > ' ') {
                value = value * 10 + (c - '0');
                c = read();
            }
            return value * sign;
        }
    }

    static class HeapNode {
        int item;
        int priority;

        HeapNode(int item, int priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    static class MinHeap {
        private HeapNode[] heap;
        private int size;
        private HashMap<Integer, Integer> Position;

        MinHeap(int capacity) {
            StartHeap(capacity);
        }

        // 3. StartHeap(N)
        void StartHeap(int capacity) {
            heap = new HeapNode[capacity + 1]; // 1-based indexing
            size = 0;
            Position = new HashMap<>(Math.max(16, capacity * 2));
        }

        boolean isEmpty() {
            return size == 0;
        }

        boolean contains(int item) {
            return Position.containsKey(item);
        }

        int size() {
            return size;
        }

        private void swap(int i, int j) {
            HeapNode temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;

            // update Position every time we swap
            Position.put(heap[i].item, i);
            Position.put(heap[j].item, j);
        }

        // 1. Heapify Up(index)
        void HeapifyUp(int index) {
            while (index > 1) {
                int parent = index / 2;
                if (heap[index].priority >= heap[parent].priority) {
                    break;
                }
                swap(index, parent);
                index = parent;
            }
        }

        // 2. Heapify Down(index)
        void HeapifyDown(int index) {
            while (true) {
                int left = index * 2;
                int right = left + 1;
                int smallest = index;

                if (left <= size && heap[left].priority < heap[smallest].priority) {
                    smallest = left;
                }
                if (right <= size && heap[right].priority < heap[smallest].priority) {
                    smallest = right;
                }
                if (smallest == index) {
                    break;
                }
                swap(index, smallest);
                index = smallest;
            }
        }

        // 4. Insert(item, value)
        void Insert(int item, int value) {
            size++;
            heap[size] = new HeapNode(item, value);
            Position.put(item, size);
            HeapifyUp(size);
        }

        // 5. FindMin()
        HeapNode FindMin() {
            if (size == 0)
                return null;
            return heap[1];
        }

        // 6. Delete(index)
        HeapNode Delete(int index) {
            if (index < 1 || index > size)
                return null;

            HeapNode removed = heap[index];

            if (index == size) {
                Position.remove(removed.item);
                heap[size] = null;
                size--;
                return removed;
            }

            swap(index, size);
            Position.remove(removed.item);
            heap[size] = null;
            size--;

            if (index <= size) {
                int parent = index / 2;
                if (index > 1 && heap[index].priority < heap[parent].priority) {
                    HeapifyUp(index);
                } else {
                    HeapifyDown(index);
                }
            }

            return removed;
        }

        // 7. ExtractMin()
        HeapNode ExtractMin() {
            if (size == 0)
                return null;
            return Delete(1);
        }

        // 8. Delete(item) -> renamed because Java int/int overload is ambiguous by
        // meaning
        HeapNode DeleteItem(int item) {
            Integer index = Position.get(item);
            if (index == null)
                return null;
            return Delete(index);
        }

        // 9. ChangePriority(item, newPriority)
        void ChangePriority(int item, int newPriority) {
            Integer index = Position.get(item);
            if (index == null)
                return;

            int oldPriority = heap[index].priority;
            heap[index].priority = newPriority;

            if (newPriority < oldPriority) {
                HeapifyUp(index);
            } else if (newPriority > oldPriority) {
                HeapifyDown(index);
            }
        }
    }

    static class Edge {
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

    static class DijkstraResult {
        int[] dist;
        ArrayList<Edge>[] prevEdges;

        DijkstraResult(int[] dist, ArrayList<Edge>[] prevEdges) {
            this.dist = dist;
            this.prevEdges = prevEdges;
        }
    }

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

        // Use our own heap, not Java PriorityQueue
        MinHeap pq = new MinHeap(n);

        // Matches the classic Dijkstra setup from the assignment:
        // all vertices are inserted, source = 0, others = INF
        for (int v = 0; v < n; v++) {
            pq.Insert(v, dist[v]);
        }

        while (!pq.isEmpty()) {
            HeapNode current = pq.ExtractMin();
            int u = current.item;

            if (current.priority == INF) {
                break; // remaining vertices are unreachable
            }

            for (Edge e : graph[u]) {
                if (e.removed)
                    continue;

                int v = e.to;

                // only process v if still in Q
                if (!pq.contains(v))
                    continue;

                int alt = dist[u] + e.weight;

                if (alt < dist[v]) {
                    dist[v] = alt;
                    prevEdges[v].clear(); // important: clear old predecessors
                    prevEdges[v].add(e); // store actual edge object
                    pq.ChangePriority(v, alt);
                } else if (alt == dist[v]) {
                    prevEdges[v].add(e); // equally short path, append
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
                e.removed = true; // remove this critical edge
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

            // First adapted Dijkstra
            DijkstraResult first = dijkstra(graph, source);

            if (first.dist[target] == INF) {
                out.append(-1).append('\n');
                continue;
            }

            // Remove all edges that belong to any shortest path
            removeCriticalEdges(first.prevEdges, target);

            // Second Dijkstra on remaining graph
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