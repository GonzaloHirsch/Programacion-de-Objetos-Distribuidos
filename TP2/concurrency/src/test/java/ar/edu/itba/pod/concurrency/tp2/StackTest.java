package ar.edu.itba.pod.concurrency.tp2;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StackTest {
    private static final int MAX_OPERATIONS = 10;
    private Stack stack;

    @Before
    public final void before() {
        this.stack = new Stack();
    }

    private final Runnable pusher = () -> {
        for (int i = 0; i < MAX_OPERATIONS; i++){
            this.stack.push(i);
        }
    };

    private final Runnable popper = () -> {
        for (int i = 0; i < MAX_OPERATIONS; i++){
            this.stack.pop();
        }
    };

    @Test
    public final void call_pusher_and_popper() {
        Thread push_thread = new Thread(this.pusher);
        Thread pop_thread = new Thread(this.popper);

        push_thread.start();
        pop_thread.start();

        assertEquals(0, this.stack.getTop());
    }

}