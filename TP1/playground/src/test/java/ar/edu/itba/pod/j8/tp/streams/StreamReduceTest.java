
package ar.edu.itba.pod.j8.tp.streams;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;

/**
 * Test to lear about {@link Stream#reduce()}
 *
 * @author Marcelo
 * @since Aug 5, 2015
 */
public class StreamReduceTest {

    @Test
    public final void reduce() {
        // TODO SOLUTION: changed expected from 0 -> 40, the sum is accumulated
        final Stream<Integer> numbers = Stream.of(5, 3, 2, 7, 3, 13, 7);
        // returns an optional.
        assertEquals("the result should be?", Integer.valueOf(40), numbers.reduce((a, b) -> a + b).get());
    }

    @Test
    public final void reduce_with_identity() {
        // TODO SOLUTION: changed expected from 0 -> 41, the sum is accumulated and starts at 1
        final Stream<Integer> numbers = Stream.of(5, 3, 2, 7, 3, 13, 7);
        // returns a value
        assertEquals("the result should be?", Integer.valueOf(41), numbers.reduce(1, (a, b) -> a + b));
    }

    @Test
    public final void reduce_with_strings() {
        // TODO SOLUTION: changed accumulation condition to x + " " + y and initial value to "Hey!"
        final Stream<String> words = Stream.of("this", "is", "a", "phrase");
        assertEquals("How do we make this", "Hey! this is a phrase", words.reduce("Hey!", (x, y) -> x + " " + y));
    }

    @Test
    public final void reduce_with_identity_combiner_parallel() {
        // TODO SOLUTION: the .parallel() method divides the stream in parallel streams in which the accumulator function is applied, and then the result of each stream is combined using the combinator function
        final Stream<Integer> numbers = Stream.of(5, 3, 2, 7, 3, 13, 7).parallel();

        // can't find the usage of the last parameter
        assertEquals("the result should be?", Integer.valueOf(7),
                numbers.reduce(1, (a, b) -> a + b, (x, y) -> x - y));
    }

}
