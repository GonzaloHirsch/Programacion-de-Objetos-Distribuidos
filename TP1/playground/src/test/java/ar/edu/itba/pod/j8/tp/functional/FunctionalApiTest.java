package ar.edu.itba.pod.j8.tp.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.Test;

import ar.edu.itba.pod.j8.tp.functional.NonFunctionalInterface;

/**
 * Test to try out the Function API Each test has a definition and some assertions that need to be fixed to
 * understand.
 *
 * Though in the test the function variables are assigned lambdas they could be used using the new Idiom but we
 * don't do it to keep the test shorts and readable:
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public final class FunctionalApiTest {

    @Test
    public final void predicates_test_conditions() {
        final Predicate<String> containsPredicate = test -> test.equals("sa");
        // TODO SOLUTION: changed test("") -> test("sa") because it is an assertTrue
        assertTrue(containsPredicate.test("sa"));
        assertFalse(containsPredicate.test(""));

        // Composition:
        assertTrue(containsPredicate.negate().test(""));

        final Predicate<String> nullPredicate = (test -> test == null);
        assertFalse(nullPredicate.or(containsPredicate).test(""));
        // TODO SOLUTION: changed to test(null) or test("sa"), both would work, the predicate composition is with or
        assertTrue(nullPredicate.or(containsPredicate).test(null));

        // FIXME: To think
        // FIXME: assertWHAT?(nullPredicate.or(containsPredicate).test(null));
        // FIXME Uncomment and explain error assertTrue(containsPredicate.or(nullPredicate).test(null));
        // TODO SOLUTION: the error is that the first predicate to be evaluated in the expression assertTrue(containsPredicate.or(nullPredicate).test(null)); is the one that performs an equals, so if we send a null value, it will cause a null pointer exception upon calling the equals method
    }

    @Test
    public final void consumer_uses_received_values() {
        final Consumer<String> consumer = (value) -> assertEquals("sa", value);
        // TODO SOLUTION: changed accept("") -> accept("sa") because it is an assertEquals
        consumer.accept("sa");
        // Composition can make a "chain" of responsibility
        // TODO SOLUTION: changed accept("") -> accept("sa") because it is an assertEquals
        // TODO NOTE: the print shows the value passed by the consumer chain
        consumer.andThen((value) -> System.out.println(value)).accept("sa");
    }

    @Test
    public final void function_reives_and_returns_values() {
        // TODO SOLUTION: changed new Integer to Integer.valueOf() in all cases
        final Function<String, Integer> intValue = (x -> Integer.parseInt(x));
        // TODO SOLUTION: the expected value is 3
        assertEquals(Integer.valueOf(3), intValue.apply("3"));
        // composition
        // TODO SOLUTION: the expected is 4 because the andThen function is called after the apply
        assertEquals(Integer.valueOf(4), intValue.andThen(x -> x + 1).apply("3"));
        // TODO SOLUTION: the expected is 31 because the compose function is called before the apply
        assertEquals(Integer.valueOf(31), intValue.compose(x -> x + "1").apply("3"));
    }

    @Test
    public final void supplier_returns_a_value() {
        final Supplier<Integer> supplier = () -> 1;
        // TODO SOLUTION: changed to Integer.valueOf and changed expected from 0 -> 1 because of the value the supplier returns
        assertEquals(Integer.valueOf(1), supplier.get());
    }

    @Test
    public final void unary_operator_receives_one_value() {
        // TODO SOLUTION: changed to Integer.valueOf in all cases
        final UnaryOperator<Integer> inc = (x -> x + 1);
        // TODO SOLUTION: the expected is 4 due to 3 + 1 = 4
        assertEquals(Integer.valueOf(4), inc.apply(3));
        // Composition:
        // TODO SOLUTION: the expected is 8 because the andThen is applied after
        assertEquals(Integer.valueOf(8), inc.andThen(x -> x * 2).apply(3));
        // TODO SOLUTION: the expected is 7 because it is first multiplied and then incremented, the type is needed because the function is applied before the apply(), so the type is not known
        assertEquals(Integer.valueOf(7), inc.compose((final Integer x) -> x * 2).apply(3)); // FIXME why the type for x?
    }

    @Test
    public final void binary_operator_receives_two_values() {
        // TODO SOLUTION: changed to Integer.valueOf in all cases
        final BinaryOperator<Integer> adder = (x, y) -> x + y;
        // TODO SOLUTION: the expected value is 9
        assertEquals(Integer.valueOf(9), adder.apply(4, 5));
        // Composition (receives a Function not an operator
        // TODO SOLUTION: the expected is 18
        assertEquals(Integer.valueOf(18), adder.andThen((x) -> x * 2).apply(4, 5));
    }

    @Test
    public final void some_more_specific_functions() {
        final DoublePredicate pressisionPredicate = x -> x < 0.001;
        assertFalse(pressisionPredicate.test(0.3));
        final IntBinaryOperator binaryOperator = (x, y) -> x - y;
        // TODO SOLUTION: the expected is -1
        assertEquals(-1, binaryOperator.applyAsInt(1, 2));
        // Many more...
    }

    @Test
    public final void functional_interface_annotation() {
        @SuppressWarnings("unused")
        final NonFunctionalInterface interFace = mock(NonFunctionalInterface.class);
        // TODO SOLUTION: uncommented the method breaking the FunctionalInterface interface annotation and then commented the annotation, then commented the line below
        //fail("go to the interface and comment the method to check the annotation function, then erase this instruction");
    }
}
