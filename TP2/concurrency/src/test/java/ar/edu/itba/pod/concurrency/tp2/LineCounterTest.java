package ar.edu.itba.pod.concurrency.tp2;

import org.junit.Test;

public class LineCounterTest {
    @Test
    public final void count_all_lines() {
        LineCounter counter = new LineCounter("/Users/gonzalo/Repository/paw-2020a-2-hp/tpe_paw/webapp/src/main/java/ar/edu/itba/paw/webapp/auth");

        counter.countAllLines();
    }
}