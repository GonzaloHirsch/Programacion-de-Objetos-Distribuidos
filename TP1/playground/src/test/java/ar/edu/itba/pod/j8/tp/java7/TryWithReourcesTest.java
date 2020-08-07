package ar.edu.itba.pod.j8.tp.java7;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test to learn the usage of Try with resource feature {
 * @link https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html}
 *
 * Basically 2 test that try to run some computation with a closeable resource. One finishes ok the other no.
 * The idea is to see what happens with the {@link Closeable#close()} method.
 *
 * @author Marcelo
 * @since Jul 28, 2015
 */
public final class TryWithReourcesTest {

    private Closeable closeableMock;

    @Before
    public void before() {
        closeableMock = mock(Closeable.class);
    }

    @Test
    public void close_should_be_called_on_normal_execution() throws IOException {
        try (Closeable closeable = closeableMock) {
            assertTrue(true); // whatever happens without error
        }
        verify_close_calls();
    }

    @Test
    public void close_should_be_called_on_abnormal_execution() throws IOException {
        try (Closeable closeable = closeableMock) {
            throw new RuntimeException("some error"); // error
        } catch (final Exception e) {
            // whatever you want to do with the error
        }
        verify_close_calls();
    }

    /** verifies the amount of calls to close method on the {@link #closeableMock} */
    private void verify_close_calls() throws IOException {
        // FIXME how many times it was called??
        // TODO SOLUTION: Changed the times(0) -> times(1) because we expect the try with resources to close the closeable 1 time
        verify(closeableMock, times(1)).close();
    }

}
