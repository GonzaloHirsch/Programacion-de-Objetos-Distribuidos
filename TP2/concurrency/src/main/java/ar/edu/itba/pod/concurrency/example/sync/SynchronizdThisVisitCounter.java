package ar.edu.itba.pod.concurrency.example.sync;

/**
 * Synchronized with this as mutex
 */
public class SynchronizdThisVisitCounter {

    private int c = 0;

    public void addVisit() {
        synchronized (this) {
            c++;
        }
    }

    public synchronized int getVisits() {
        synchronized (this) {
            return c;
        }
    }

    public int peekVisits() {
        return c;
    }

}
