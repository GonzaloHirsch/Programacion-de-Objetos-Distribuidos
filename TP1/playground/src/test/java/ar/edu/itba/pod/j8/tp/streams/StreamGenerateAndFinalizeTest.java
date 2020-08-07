package ar.edu.itba.pod.j8.tp.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.junit.Test;

/**
 * Test to learn about stream construction and finalization.
 * 
 * @author Marcelo
 * @since Aug 5, 2015
 */
public class StreamGenerateAndFinalizeTest {

    @Test
    public final void construct_from_collection() {
        // TODO SOLUTION: added call to .count()
        final Stream<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5).stream();
        assertEquals("should be getting the stream element count", 5, numbers.count());
    }

    @Test
    public final void of_element_find_first() {
        // TODO SOLUTION: changed to Integer.valueOf and added call to .findFirst() to get the optional
        final Stream<Integer> numbers = Stream.of(34);
        assertEquals("Should be getting the first element", Integer.valueOf(34), numbers.findFirst().orElse(-1));
    }

    @Test
    public final void builder_find_any() {
        // TODO SOLUTION: added call to .findAny()
        Builder<Integer> builder = Stream.builder();
        Stream<Integer> numbers = builder.add(1).add(2).add(3).add(4).add(5).build();
        // find any does not guarantee a stable result
        assertTrue("use find any to match", Arrays.asList(1, 2, 3, 4, 5).contains(numbers.findAny().orElse(-1)));
    }

    @Test
    public final void of_list_any_match() {
        // TODO SOLUTION: added call to anyMatch(x -> x == 2) to se if any matches that item
        final Stream<Integer> numbers = Stream.of(32, 4, 12, 3, 2, 1);
        assertEquals("Should be true if any match 2", true, numbers.anyMatch(x -> x == 2));
    }

    @Test
    public final void iterate_all_match() {
        // TODO SOLUTION: added call to .allMatch(x -> x % 2 == 1)
        final Stream<Integer> numbers = Stream.iterate(1, x -> x + 3); // infinite stream
        // as all match will break with the first non complient it will not be an infinite loop.
        assertEquals("Use all match to uneven", false, numbers.allMatch(x -> x % 2 == 1));
    }

    @Test
    public final void empty_none_match() {
        // TODO SOLUTION: added call to .noneMatch(x -> x == 1)
        final Stream<Integer> numbers = Stream.empty();
        assertEquals("None match should match to 1", true, numbers.noneMatch(x -> x == 1));
    }

    @Test
    public final void concat_min_max() {
        // TODO SOLUTION: added a Supplier to avoid the error of using a closed stream, also added call to .min(Comparator.naturalOrder()).orElse(-1) and .max(Comparator.naturalOrder()).orElse(-1)
        final Supplier<Stream<Integer>> numbersSupplier = () -> Stream.concat(Stream.of(32), Stream.of(31, 33));
        assertEquals("Should return the min", Integer.valueOf(31), numbersSupplier.get().min(Comparator.naturalOrder()).orElse(-1)); // check Comparator#naturalOrder
        assertEquals("Should return the max", Integer.valueOf(33), numbersSupplier.get().max(Comparator.naturalOrder()).orElse(-1)); // check Comparator#naturalOrder
    }

    @Test
    public final void int_stream_average() {
        // TODO NOTE: no error here
        final IntStream numbers = IntStream.of(4, 2, 13, 4, 5);
        assertEquals("should get the average", 5.6, numbers.average().getAsDouble(), 0.01);

        // TODO SOLUTION: added call to .getMax() and .getMin()
        // summary statistics
        IntSummaryStatistics summaryStatistics = IntStream.of(4, 2, 13, 4, 5).summaryStatistics();
        assertEquals("should get the maxium", 13, summaryStatistics.getMax());
        assertEquals("should get the minimum", 2, summaryStatistics.getMin());
        assertEquals(5.6, summaryStatistics.getAverage(), 0.001);
    }

    @Test
    public final void double_stream_sum() {
        // TODO SOLUTION: added call to .sum()
        final DoubleStream numbers = DoubleStream.of(2.5, 1.2, 3.2);
        assertEquals("should get the sum of the values", 6.9, numbers.sum(), 0.001);
    }
}
