package ar.edu.itba.pod.concurrency.e1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link GenericService} and {@link GenericServiceImpl}.
 */
public class GenericServiceTest {
    private GenericService service;

    @Before
    public final void before() {
        service = new GenericServiceImpl();
    }

    @Test
    public final void echo_should_echo_the_message() {
        assertEquals("hello", service.echo("hello"));
    }

    @Test
    public final void echo_of_null() {
        assertNull(service.echo(null));
    }

    @Test
    public final void to_upper_of_message() {
        assertEquals("HELLO", service.toUpper("hello"));
    }

    @Test
    public final void to_upper_of_null() {
        assertNull(service.toUpper(null));
    }

    @Test
    public final void visit_count_should_start_in_zero() {
        assertEquals(0, service.getVisitCount());
    }

    @Test
    public final void visit_count_should_reflect_visits() {
        for (int i = 1; i < 100; i++) {
            service.addVisit();
            assertEquals(i, service.getVisitCount());
        }
    }

    @Test
    public final void queue_should_start_empty() {
        assertTrue(service.isServiceQueueEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public final void queue_throws_on_get_empty() {
        service.getFirstInServiceQueue();
    }

    @Test
    public final void queue_should_return_in_order() {
        final List<String> names = Arrays.asList("John", "Ringo", "Paul", "George", "Jimmy", "John",
                "John Paul", "Robert", "Roger", "John", "Keith", "Pete", "Mick", "Keith", "Ronnie",
                "Charlie");

        names.forEach(name -> service.addToServiceQueue(name));

        for (int i = 0; i < names.size(); i++) {
            assertEquals(names.get(i), service.getFirstInServiceQueue());
        }
    }
}
