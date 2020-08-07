package ar.edu.itba.pod.j8.tp.functional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test to review the different types of methods reference
 *
 * Bare in mind that manily the use will not be to assing the references to variable but to pass them as
 * parameter to methods that receive Consumers, or Suppliers to avoid creating lambdas when a method already
 * exists that performs the wanted action.
 *
 * But the test assings to keep it simple and see what types of references can be use with a certain type of
 * parameters.
 *
 * As usual the idea is to make the test pass while figuring out what the functionality is..
 *
 * @author Marcelo
 * @since Jul 31, 2015
 */
public class MethodReferenceTest {

    // TODO SOLUTION: changed to Integer.valueOf in all cases

    @Test
    public final void to_static_method() {
        final BinaryOperator<Integer> sum = Integer::sum;
        // TODO SOLUTION: expected value is 3
        assertEquals(Integer.valueOf(3), sum.apply(1, 2));
    }

    @Test
    public final void to_specified_instance_method() {
        final String text = "hola";
        final Supplier<String> upperfier = text::toUpperCase;
        // TODO SOLUTION: expected value is HOLA
        assertEquals("HOLA", upperfier.get());
    }

    @Test
    public final void to_unspecified_instance_method() {
        final Function<String, String> upperfier = String::toUpperCase;
        // TODO SOLUTION: expected value is HOLA
        assertEquals("HOLA", upperfier.apply("hola"));
    }

    @Test
    public final void to_constructor() {
        final Supplier<Set<Integer>> constructor = HashSet<Integer>::new;
        Set<Integer> set = constructor.get();
        assertNotNull(set);
        // TODO SOLUTION: expected value for size is 0 due to recently created constructor
        assertEquals(0, set.size());

        // infers the constructor from the type of the variable!!!!!
        final Function<Collection<Integer>, Set<Integer>> otherConstructor = HashSet<Integer>::new;
        set = otherConstructor.apply(Arrays.asList(1, 2, 3));
        assertNotNull(set);
        // TODO SOLUTION: expected value for size is 3
        assertEquals(3, set.size());
        // FIXME assertWHAT(set.contains(1));
        // TODO SOLUTION: we are asserting for true
        assertTrue(set.contains(1));
    }

    @Test
    public final void to_array_constructor() {
        final IntFunction<int[]> arrayMaker = int[]::new;
        // TODO SOLUTION: expected value for length is 10, the value is used for the size
        assertEquals(10, arrayMaker.apply(10).length);
    }
}
