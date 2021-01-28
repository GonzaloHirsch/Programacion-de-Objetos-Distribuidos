package ar.edu.itba.pod.server;

import ar.edu.itba.pod.GenericService;
import ar.edu.itba.pod.User;
import ar.edu.itba.pod.UserAvailableCallbackHandler;

import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class GenericServiceImpl implements GenericService {

    private int visitCount = 0;
    private final Queue<String> serviceQueue = new ArrayDeque<>();
    private final String LOCK = "LOCK";
    private final Queue<User> userServiceQueue = new ArrayDeque<>();
    private final Queue<UserAvailableCallbackHandler> handlerQueue = new ArrayDeque<>();

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
            synchronized (this.handlerQueue) {
                if (!this.handlerQueue.isEmpty()) {
                    this.getFirstUserInServiceQueue(Optional.ofNullable(this.handlerQueue.poll()).orElseThrow(() -> new IllegalStateException("Inconsistent read")));
                }
            }
        }
    }

    @Override
    public void getFirstUserInServiceQueue(UserAvailableCallbackHandler handler) throws RemoteException {
        synchronized (this.userServiceQueue) {
            if (this.userServiceQueue.isEmpty()) {
                synchronized (this.handlerQueue) {
                    this.handlerQueue.add(handler);
                }
            } else {
                handler.userAvailable(Optional.ofNullable(this.userServiceQueue.poll()).orElseThrow(() -> new IllegalStateException("Empty queue")));
            }
        }
    }
}
