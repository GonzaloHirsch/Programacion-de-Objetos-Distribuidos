package server_1;

import client_1.GenericService;
import client_1.User;

import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

/**
 * Basic implementation of {@link ar.edu.itba.pod.concurrency.e1.GenericService}.
 */
public class GenericServiceImpl implements GenericService {

    private int visitCount = 0;
    private final Queue<String> serviceQueue = new ArrayDeque<>();
    private final String LOCK = "LOCK";
    private final Queue<User> userServiceQueue = new ArrayDeque<>();

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

    @Override
    public boolean isUserServiceQueueEmpty() throws RemoteException {
        synchronized (this.userServiceQueue) {
            return this.userServiceQueue.isEmpty();
        }
    }

    @Override
    public void addUserToServiceQueue(User user) throws RemoteException {
        synchronized (this.userServiceQueue) {
            this.userServiceQueue.add(user);
        }
    }

    @Override
    public User getFirstUserInServiceQueue() throws RemoteException {
        synchronized (this.userServiceQueue) {
            return Optional.ofNullable(this.userServiceQueue.poll()).orElseThrow(() -> new IllegalStateException("Empty queue"));
        }
    }
}
