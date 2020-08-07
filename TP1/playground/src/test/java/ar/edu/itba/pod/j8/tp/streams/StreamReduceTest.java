
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
        final Stream<Integer> numbers = Stream.of(5, 3, 2, 7, 3, 13, 7);
        // returns an optional.
        assertEquals("the result should be?", new Integer(0), numbers.reduce((a, b) -> a + b).get());

    }

    @Test
    public final void reduce_with_identity() {
        final Stream<Integer> numbers = Stream.of(5, 3, 2, 7, 3, 13, 7);
        // returns a value
        assertEquals("the result should be?", new Integer(0), numbers.reduce(1, (a, b) -> a + b));
    }

    @Test
    public final void reduce_with_strings() {
        final Stream<String> words = Stream.of("this", "is", "a", "phrase");
        assertEquals("How do we make this", "Hey! this is a phrase", words.reduce("", (x, y) -> x));
    }

    @Test
    public final void reduce_with_identity_combiner_parallel() {
        final Stream<Integer> numbers = Stream.of(5, 3, 2, 7, 3, 13, 7).parallel();

        // can't find the usage of the last parameter
        assertEquals("the result should be?", new Integer(0),
                numbers.reduce(1, (a, b) -> a + b, (x, y) -> x - y));
    }

}
