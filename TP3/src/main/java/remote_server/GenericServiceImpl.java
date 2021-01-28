package remote_server;

import remote_client.GenericService;

import java.util.*;

/**
 * Basic implementation of {@link ar.edu.itba.pod.concurrency.e1.GenericService}.
 */
public class GenericServiceImpl implements GenericService {

    private int visitCount = 0;
    private final Queue<String> serviceQueue = new ArrayDeque<>();
    private final String LOCK = "LOCK";

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
        synchronized (this.LOCK) {
            this.visitCount++;
        }
    }

    @Override
    public int getVisitCount() {
        synchronized (this.LOCK) {
            return this.visitCount;
        }
    }

    @Override
    public boolean isServiceQueueEmpty() {
        synchronized (this.serviceQueue) {
            return this.serviceQueue.isEmpty();
        }
    }

    @Override
    public void addToServiceQueue(String name) {
        synchronized (this.serviceQueue) {
            this.serviceQueue.add(name);
        }
    }

    @Override
    public String getFirstInServiceQueue() {
        synchronized (this.serviceQueue) {
            return Optional.ofNullable(this.serviceQueue.poll()).orElseThrow(() -> new IllegalStateException("Empty queue"));
        }
    }
}
