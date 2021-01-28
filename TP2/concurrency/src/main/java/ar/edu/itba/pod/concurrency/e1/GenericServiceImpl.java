package ar.edu.itba.pod.concurrency.e1;

import java.util.*;

/**
 * Basic implementation of {@link GenericService}.
 */
public class GenericServiceImpl implements GenericService {

    private int visitCount = 0;
    private final Queue<String> serviceQueue = new ArrayDeque<>();

    @Override
    public String echo(String message) {
        return message;
    }

    @Override
    public String toUpper(String message) {
        return Optional.ofNullable(message).map(m -> m.toUpperCase()).orElse(null);
    }

    @Override
    public void addVisit() {
        this.visitCount++;
    }

    @Override
    public int getVisitCount() {
        return this.visitCount;
    }

    @Override
    public boolean isServiceQueueEmpty() {
        return this.serviceQueue.isEmpty();
    }

    @Override
    public void addToServiceQueue(String name) {
        this.serviceQueue.add(name);
    }

    @Override
    public String getFirstInServiceQueue() {
        return Optional.ofNullable(this.serviceQueue.poll()).orElseThrow(() -> new IllegalStateException("Empty queue"));
    }
}
