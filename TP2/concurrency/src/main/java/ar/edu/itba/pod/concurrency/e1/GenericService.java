package ar.edu.itba.pod.concurrency.e1;

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
public interface GenericService {

    /** @return the message given as parameter */
    String echo(String message);

    /** @return the message with upper case or null if null was passed */
    String toUpper(String message);

    /** adds one to the visit counter */
    void addVisit();

    /** @return the current visit count */
    int getVisitCount();

    /** @return true if the queue is empty - */
    boolean isServiceQueueEmpty();

    /**
     * Adds the name to a queue.
     * 
     * @throws NullPointerException
     *             if the name is null.
     */
    void addToServiceQueue(String name);

    /**
     * @returns the name at the beggining of the queue.
     * @throws IllegalStateException
     *             if queue is empty.
     */
    String getFirstInServiceQueue();
}
