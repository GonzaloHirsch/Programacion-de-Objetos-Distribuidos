package client_1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz for a generic service with 3 categories of methods:
 *
 * <ul>
 * <li>String methods represented by {@link #echo(String)} and
 * {@link #toUpper(String)}</li>
 * <li>Visit Count methods: to have a counter where the service stores the
 * amount of visits according to the usage of {@link #getVisitCount()} and
 * {@link #addVisit()}</li>
 * <li>Service Queue methods: The implementation must have a queue</li>
 * </ul>
 */
public interface GenericService extends Remote {

    /** @return the message given as parameter */
    String echo(String message) throws RemoteException;

    /** @return the message with upper case or null if null was passed */
    String toUpper(String message) throws RemoteException;

    /** adds one to the visit counter */
    void addVisit() throws RemoteException;

    /** @return the current visit count */
    int getVisitCount() throws RemoteException;

    /** @return true if the queue is empty - */
    boolean isServiceQueueEmpty() throws RemoteException;

    /**
     * Adds the name to a queue.
     *
     * @throws NullPointerException
     *             if the name is null.
     */
    void addToServiceQueue(String name) throws RemoteException;

    /**
     * @returns the name at the beggining of the queue.
     * @throws IllegalStateException
     *             if queue is empty.
     */
    String getFirstInServiceQueue() throws RemoteException;

    /** @return true if the queue is empty - */
    boolean isUserServiceQueueEmpty() throws RemoteException;

    /**
     * Adds the name to a queue.
     *
     * @throws NullPointerException
     *             if the name is null.
     */
    void addUserToServiceQueue(User user) throws RemoteException;

    /**
     * @returns the name at the beggining of the queue.
     * @throws IllegalStateException
     *             if queue is empty.
     */
    User getFirstUserInServiceQueue() throws RemoteException;
}
