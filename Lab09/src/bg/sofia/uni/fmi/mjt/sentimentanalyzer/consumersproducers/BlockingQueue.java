package bg.sofia.uni.fmi.mjt.sentimentanalyzer.consumersproducers;

import java.util.ArrayDeque;
import java.util.Queue;

public class BlockingQueue<T> {

    private final Queue<T> blockingQueue;
    private final Integer maxCapacity;

    public BlockingQueue(Integer maxCapacity) {
        this.blockingQueue = new ArrayDeque<>();
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be bigger than 0!");
        }
        this.maxCapacity = maxCapacity;
    }

    public synchronized T poll() {
        while (isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("The thread was interrupted while waiting to poll from the queue", e);
            }
        }

        this.notifyAll();
        return this.blockingQueue.poll();
    }

    public synchronized boolean add(T el) {
        while (size() >= maxCapacity) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("The thread was interrupted while waiting to add to the queue", e);
            }
        }

        this.notifyAll();
        return this.blockingQueue.add(el);
    }

    private boolean isEmpty() {
        return this.blockingQueue.isEmpty();
    }

    private Integer size() {
        return this.blockingQueue.size();
    }

}