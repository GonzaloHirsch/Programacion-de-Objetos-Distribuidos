package ar.edu.itba.pod.concurrency.e2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.pod.concurrency.e1.GenericService;
import ar.edu.itba.pod.concurrency.e1.GenericServiceImpl;

/**
 * Unit test for {@link GenericService} using {@link Thread}s
 */
public class GenericServiceConcurrencyTest {
    private static final int EXPECTED_VISITS = 50000;
    private static final int VISITS_COUNT = 10000;

    private GenericService service;

    @Before
    public final void before() {
        service = new GenericServiceImpl();
    }

    /** Realiza 100 visitas al servicio */
    private final Runnable visitor = () -> {
        for (int i = 0; i < VISITS_COUNT; i++){
            this.service.addVisit();
        }
    };

    // instanciar el pool.
    private final ExecutorService pool = Executors.newFixedThreadPool(5);

    /**
     * generates 5 threads with {@link #visitor} and runs them.
     */
    @Test
    public final void visit_count_with_thread_start() throws InterruptedException {
        // Add threads and run them
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            threads.add(new Thread(visitor));
            threads.get(i).start();
        }
        // Join threads
        for (Thread thread : threads) {
            thread.join();
        }
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates 5 threads with {@link #visitor} and runs them submiting it via
     * the {@link ExecutorService}
     */
    @Test
    public final void visit_count_with_executor_submit() throws InterruptedException {
        // Submitting all threads
        for (int i = 0; i < 5; i++) {
            this.pool.submit(visitor);
        }
        // Awaiting all threads
        this.pool.shutdown();
        this.pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates 5 threads with {@link #visitor} and runs with
     * {@link ExecutorService#invokeAll(Collection)}
     */
    @Test
    public final void visit_count_with_executor_invoke() throws InterruptedException {
        // Adding all threads
        Collection<Callable<Void>> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            threads.add(() -> {
                visitor.run();
                return null;
            });
        }
        // Invoking all threads
        this.pool.invokeAll(threads);
        // Awaiting all threads
        this.pool.shutdown();
        this.pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates 5 Threads of a Runnable that sleesps for 10 seconds an inserts
     * 10 elements into the service queue. Once all threads are done, the test
     * should check to see of the queue is empty
     */
    @Test
    public final void concurrent_queue() throws InterruptedException {
        final List<String> names = Arrays.asList("John", "Ringo", "Paul", "George", "Jimmy", "John",
                "John Paul", "Robert", "Roger", "John");

        // Submitting all threads
        for (int i = 0; i < 5; i++) {
            this.pool.submit(() -> {
                try {
                    Thread.sleep(10000);
                    names.forEach(name -> service.addToServiceQueue(name));
                    for (int j = 0; j < names.size(); j++){
                        service.getFirstInServiceQueue();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // Awaiting all threads
        this.pool.shutdown();
        this.pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
        assertTrue(service.isServiceQueueEmpty());
    }
}
