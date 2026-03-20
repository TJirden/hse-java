package hse.java.lectures.lecture6.tasks.queue;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBlockingQueue<T> {

    private final Queue<T> queue;
    private final int cap;

    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0");
        }
        this.cap = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void put(T item) throws InterruptedException {
        if (item == null) {
            throw new NullPointerException("null is not allowed");
        }
        while (queue.size() == cap) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.poll();
        notifyAll();
        return item;
    }

    public synchronized int size() {
        return queue.size();
    }

    public int capacity() {
        return cap;
    }
}