package ar.edu.itba.pod.concurrency.e4;

import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * Benchmar to compare between {@link Arrays#parallelSort(int[])} and
 * {@link Arrays#sort(int[])}
 */
public class SortBenchmark {

    private static final int ARR_BIG = 50000000;
    private static final int ARR_MEDIUM = 25000000;
    private static final int ARR_SMALL = 10000000;

    private final Function<int[], int[]> array_copier = a -> Arrays.copyOf(a, a.length);

    private int[] generateArray(int length){
        int[] arr = new int[length];
        for (int i = 0; i < length; i++){
            arr[i] = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        return arr;
    }

    private long time_sort_serial(int[] arr){
        Instant start = Instant.now();
        Arrays.sort(arr);
        Instant end = Instant.now();
        return (end.toEpochMilli() - start.toEpochMilli());
    }

    private long time_sort_parallel(int[] arr){
        Instant start = Instant.now();
        Arrays.parallelSort(arr);
        Instant end = Instant.now();
        return (end.toEpochMilli() - start.toEpochMilli());
    }

    private void benchmark_size(int size){
        String prefix = size == ARR_BIG ? "BIG" : (size == ARR_MEDIUM ? "MEDIUM" : "SMALL");

        double partial;

        int[] arr = this.generateArray(size);

        // Serial
        partial = this.time_sort_serial(array_copier.apply(arr));
        partial += this.time_sort_serial(array_copier.apply(arr));
        partial += this.time_sort_serial(array_copier.apply(arr));
        partial += this.time_sort_serial(array_copier.apply(arr));
        partial /= 4;
        System.out.println(prefix + " - Normal Sort - " + partial + " ms");

        // Parallel
        partial = this.time_sort_parallel(array_copier.apply(arr));
        partial += this.time_sort_parallel(array_copier.apply(arr));
        partial += this.time_sort_parallel(array_copier.apply(arr));
        partial += this.time_sort_parallel(array_copier.apply(arr));
        partial /= 4;
        System.out.println(prefix + " - Parallel Sort - " + partial + " ms");
    }

    @Test
    public void benchmark_big() {
        this.benchmark_size(ARR_BIG);
    }

    @Test
    public void benchmark_medium() {
        this.benchmark_size(ARR_MEDIUM);
    }

    @Test
    public void benchmark_small() {
        this.benchmark_size(ARR_SMALL);
    }
}
