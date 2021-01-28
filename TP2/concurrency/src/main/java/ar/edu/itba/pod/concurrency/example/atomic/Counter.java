package ar.edu.itba.pod.concurrency.example.atomic;

/**
 * Counter interface.
 */
public interface Counter {
    long getCounter();

    void increment();
}
