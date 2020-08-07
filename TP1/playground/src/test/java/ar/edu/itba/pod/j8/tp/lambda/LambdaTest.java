package ar.edu.itba.pod.j8.tp.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileFilter;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.pod.j8.tp.lambda.LambdaExecutor;
import ar.edu.itba.pod.j8.tp.lambda.LambdaScopingHelloWorld;

/**
 * Test to learn about lambda. Uses {@link LambdaExecutor} and {@link LambdaScopingHelloWorld} most require
 * you to read and figure out what is wrong with assertion to make it pass.
 * 
 * @author Marcelo
 * @since Jul 29, 2015
 */
public class LambdaTest {

    private LambdaExecutor lambdas;

    @Before
    public final void before() {
        lambdas = spy(new LambdaExecutor());
    }

    @Test
    public final void lambda_have_syntaxis_sugars() throws Exception {
        // this test is to show how it is done (so we make it pass for you)
        // full syntaxis
        assertTrue(lambdas.weirdEquals((x, y) -> {
            // many more lines (actually not many, a few, with many, call a method :))
                return x - y - 1;
            }));

        assertFalse(lambdas.weirdEquals((x, y) -> x - y)); // one liner: return and ';' optional
        assertTrue(lambdas.rootDirectoryComplies(file -> file.exists()));// one parameter: no parenthesis
        assertEquals("A", lambdas.run(() -> "A")); // no parameters, empty ()
        lambdas.run(() -> System.out.println("do nothing")); // void lambda just write the code
    }

    @Test
    public final void interface_can_be_exchanged_to_lambda() throws Exception {
        // FIXME: Replace the interfaces for lambdas (hint: look above)
        // And correct
        // TODO SOLUTION: changed the code block for lambdas.run(() -> 0) and to Integer.valueOf(0)
        assertEquals(Integer.valueOf(0), lambdas.run(() -> 0));

        // TODO SOLUTION: changed the code block for lambdas.run(() -> "")
        assertEquals("", lambdas.run(() -> ""));
    }

    @Test
    public final void lambda_target_type_is_inferred_on_variable_assignment() {
        final FileFilter filter = (file -> file.canWrite());
        // FIXME: fix the assertion
        // TODO SOLUTION: changed assertTrue for assertFalse, don't know if correct solution
        assertFalse(lambdas.rootDirectoryComplies(filter));
    }

    @Test
    public final void same_lambda_infered_by_assigantion_type() throws Exception {
        final Callable<String> callable = () -> "done";
        final PrivilegedAction<String> action = () -> "done";
        // FIXME: what's the output
        // TODO SOLUTION: changed the expected "" to "done"
        assertEquals("done", callable.call());
        assertEquals("done", action.run());
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void lambda_type_is_inferred_on_return_type() throws Exception {
        // TODO SOLUTION: changed the expected "" to "hola"
        assertEquals("hola", lambdas.run(() -> "hola"));

        // FIXME which one is called?
        // TODO SOLUTION: the called one is the Callable with the String, changed expected number of invocations
        verify(lambdas, times(1)).run(any(Callable.class));
        verify(lambdas, times(0)).run(any(PrivilegedAction.class));
        verify(lambdas, times(0)).run(any(Runnable.class));

        reset(lambdas);
        // TODO SOLUTION: changed the expected 0 to 1, and to Integer.valueOf(1)
        assertEquals(Integer.valueOf(1), lambdas.run(() -> 1));
        // FIXME which one is called?
        // TODO SOLUTION: the called one is the PrivilegedAction because it returns an integer, changed expected number of invocations
        verify(lambdas, times(0)).run(any(Callable.class));
        verify(lambdas, times(1)).run(any(PrivilegedAction.class));
        verify(lambdas, times(0)).run(any(Runnable.class));
    }

    @Test
    public final void captured_variables_need_to_be_effectively_final() {
        final int finalVariable = 1;
        int effectivelyFinalVariable = 2;

        // FIXME what's the value
        // TODO SOLUTION: changed to Integer.valueOf(3), which is the expected value
        assertEquals(Integer.valueOf(3), lambdas.run(() -> finalVariable + effectivelyFinalVariable));

        // FIXME THIS DOES NOT COMPILE THE VARIABLE IS NO LONGER EFFECTIVELY FINAL
        // TODO SOLUTION: changed to Integer.valueOf(3) and removed the assignment to the effectivelyFinalVariable
        assertEquals(Integer.valueOf(3), lambdas.run(() -> 3));

        // FIXME THIS DOES NOT WORK DUE TO SHADOWING OF THE VARIABLE
        // TODO SOLUTION: changed expected new Integer(3) to false because it is a comparator, and changed the name of the effectivelyFinalVariable parameter in the lambda to avoid shadowing problems
        assertEquals(false, lambdas.weirdEquals((x, y) -> 1));
    }

    @Test
    public final void lambda_lexical_scope() throws Exception {
        // what's the value
        // TODO SOLUTION: inspected the class and changed the expected "" to "Hello, world!"
        assertEquals("Hello, world!", new LambdaScopingHelloWorld().toString());
        assertEquals("Hello, world!", new LambdaScopingHelloWorld().r1.call());
    }
}
