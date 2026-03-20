package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.List;

public class StreamingMonitor {
    private final List<Integer> sortedIds;
    private int currentIndex;

    private int totalTicksDone;
    private final int totalTicksRequired;

    private boolean finished;

    public StreamingMonitor(List<Integer> sortedIds, int ticksPerWriter) {
        this.sortedIds = sortedIds;
        this.currentIndex = 0;
        this.totalTicksRequired = sortedIds.size() * ticksPerWriter;
        this.totalTicksDone = 0;
        this.finished = false;
    }

    private int currentId() {
        return sortedIds.get(currentIndex);
    }

    public synchronized boolean awaitTurn(int writerId) {
        while (currentId() != writerId && !finished) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return !finished;
    }

    public synchronized void tickDone() {
        totalTicksDone++;

        currentIndex = (currentIndex + 1) % sortedIds.size();

        if (totalTicksDone >= totalTicksRequired) {
            finished = true;
        }

        notifyAll();
    }

    public synchronized void awaitFinished() {
        while (!finished) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}