package ar.edu.itba.pod.j8.tp.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
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
        final Stream<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5).stream();
        assertEquals("should be getting the stream element count", 5, numbers);
    }

    @Test
    public final void of_element_find_first() {
        final Stream<Integer> numbers = Stream.of(34);
        assertEquals("Should be getting the first element", new Integer(34), numbers);
    }

    @Test
    public final void builder_find_any() {
        Builder<Integer> builder = Stream.builder();
        Stream<Integer> numbers = builder.add(1).add(2).add(3).add(4).add(5).build();
        // find any does not guarantee a stable result
        assertTrue("use find any to match", Arrays.asList(1, 2, 3, 4, 5).contains(numbers));
    }

    @Test
    public final void of_list_any_match() {
        final Stream<Integer> numbers = Stream.of(32, 4, 12, 3, 2, 1);
        assertEquals("Should be true if any match 2", true, numbers);
    }

    @Test
    public final void iterate_all_match() {
        final Stream<Integer> numbers = Stream.iterate(1, x -> x + 3); // infinite stream
        // as all match will break with the first non complient it will not be an infinite loop.
        assertEquals("Use all match to uneven", false, numbers);
    }

    @Test
    public final void empty_none_match() {
        final Stream<Integer> numbers = Stream.empty();
        assertEquals("None match should match to 1", true, numbers);
    }

    @Test
    public final void concat_min_max() {
        final Stream<Integer> numbers = Stream.concat(Stream.of(32), Stream.of(31, 33));
        assertEquals("Should return the min", new Integer(31), numbers); // check Comparator#naturalOrder
        assertEquals("Should return the max", new Integer(34), numbers); // check Comparator#naturalOrder
    }

    @Test
    public final void int_stream_average() {
        final IntStream numbers = IntStream.of(4, 2, 13, 4, 5);
        assertEquals("should get the average", new Double(5.6), numbers.average().getAsDouble(), 0.01);
        // summary statistics
        IntSummaryStatistics summaryStatistics = IntStream.of(4, 2, 13, 4, 5).summaryStatistics();
        assertEquals("should get the maxium", 13, summaryStatistics);
        assertEquals("should get the minimum", 2, summaryStatistics);
        assertEquals(new Double(5.6), summaryStatistics.getAverage(), 0.001);
    }

    @Test
    public final void double_stream_sum() {
        final DoubleStream numbers = DoubleStream.of(2.5, 1.2, 3.2);
        assertEquals("should get the  sum of the values", 6.9, numbers.average().getAsDouble(), 0.001);
    }
}
