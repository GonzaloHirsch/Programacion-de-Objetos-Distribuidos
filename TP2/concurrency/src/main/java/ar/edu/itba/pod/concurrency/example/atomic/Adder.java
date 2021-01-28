package ar.edu.itba.pod.concurrency.example.atomic;

import java.util.concurrent.atomic.LongAdder;

/**
 * {@link Counter} with {@link LongAdder}
 */
public class Adder implements Counter {
    private final LongAdder adder = new LongAdder();

    public long getCounter() {
        return adder.longValue();
    }

    public void increment() {
        adder.increment();
    }
}
