package ar.edu.itba.pod.concurrency.example.future;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FutureExample {
    private static final Logger logger = LoggerFactory.getLogger(FutureExample.class);

    private static Callable<Integer> task = () -> {
        try {
            TimeUnit.SECONDS.sleep(1); // sleeps some time to "simulate" long
                                       // running tasks
            return new Random().nextInt(143);
        } catch (final InterruptedException e) {
            throw new IllegalStateException("task interrupted", e);
        }
    };

    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        final Future<Integer> future = executor.submit(task);

        for (int i = 0; i < 5; i++) {
            try {
                logger.info("attemp number {}", i);
                final Integer result = future.get(500, TimeUnit.MILLISECONDS);

                logger.info("future done? {}", future.isDone());
                if (future.isDone()) {
                    logger.info("result: {}", result);
                    executor.shutdown();
                    return;
                }
            } catch (final TimeoutException e) {
                logger.debug("timeout on attemp number {}", i);
            }
        }
        executor.shutdown();
        logger.error("Max attemps reached on timeout :(");
    }
}
