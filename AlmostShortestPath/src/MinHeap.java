import java.util.HashMap;

public class MinHeap {
    private HeapNode[] heap;
    private int size;
    private HashMap<Integer, Integer> Position;

    MinHeap(int capacity) {
        StartHeap(capacity);
    }

    void StartHeap(int capacity) {
        heap = new HeapNode[capacity + 1];
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

        Position.put(heap[i].item, i);
        Position.put(heap[j].item, j);
    }

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

    void Insert(int item, int value) {
        size++;
        heap[size] = new HeapNode(item, value);
        Position.put(item, size);
        HeapifyUp(size);
    }

    HeapNode FindMin() {
        if (size == 0)
            return null;
        return heap[1];
    }

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

    HeapNode ExtractMin() {
        if (size == 0)
            return null;
        return Delete(1);
    }

    HeapNode DeleteItem(int item) {
        Integer index = Position.get(item);
        if (index == null)
            return null;
        return Delete(index);
    }

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