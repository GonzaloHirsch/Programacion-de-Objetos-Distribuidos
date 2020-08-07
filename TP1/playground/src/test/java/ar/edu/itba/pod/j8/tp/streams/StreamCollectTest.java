package ar.edu.itba.pod.j8.tp.streams;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.pod.j8.tp.model.Person;
import ar.edu.itba.pod.j8.tp.model.Person.Sex;

/**
 * Test to learn about {@link Stream#collect(java.util.stream.Collector)}
 *
 * @author Marcelo
 * @since Aug 5, 2015
 */
public class StreamCollectTest {
    private Stream<Integer> ages;

    @Before
    public final void before() {
        ages = Stream.of(5, 12, 33, 23, 51, 78, 15);
    }

    @Test
    public void collect_with_consumer() {
        final double average = ages.collect(Averager::new, Averager::accept, Averager::combine).average();
        assertEquals("what's  the average", new Double(0), average, 0.001);
    }

    @Test
    public final void average_with_collector() {
        final Double averege = ages.collect(Collectors.averagingInt(x -> x));
        assertEquals("what's  the average", new Double(0), averege, 0.001);
    }

    @Test
    public final void sum_with_collector() {
        final Integer sum = ages.collect(Collectors.summingInt(x -> x));
        assertEquals("what's the sum", new Integer(0), sum);
    }

    @Test
    public final void joining_with_collector() {
        final Stream<String> words = Stream.of("this", "is", "a", "phrase");
        final String phrase = words.collect(Collectors.joining(", "));
        assertEquals("what's the phrase", "", phrase);
    }

    @Test
    public final void grouping_with_collector() {
        final List<Person> roster = Arrays.asList(new Person("jack", LocalDate.of(1999, 1, 1), Sex.MALE,
                "j@mail.com"), new Person("danielle", LocalDate.of(1992, 12, 1), Sex.FEMALE, "d@mail.com"),
                new Person("livy", LocalDate.of(1989, 5, 12), Sex.FEMALE, "l@mail.com"));

        final Map<Sex, List<Person>> divided = roster.stream().collect(
                Collectors.groupingBy(Person::getGender));

        assertEquals("which are females", Arrays.asList(), divided.get(Sex.FEMALE));
        assertEquals("which are males", Arrays.asList(), divided.get(Sex.MALE));
    }

    @Test
    public final void partitioning_with_collector() {
        final Map<Boolean, List<Integer>> map = ages.collect(Collectors.partitioningBy(x -> x < 22));

        assertEquals("which are less than 22", Arrays.asList(), map.get(true));
        assertEquals("which are more than 22", Arrays.asList(), map.get(false));
    }

    @Test
    public final void collect_a_list_with_collectors_consumer() {
        final List<Integer> agesList = ages.collect(Collectors.toList());
        assertEquals("what are the elements of the list", Arrays.asList(), agesList);
    }

    @Test
    public final void collect_a_list_with_consumer() {
        final ArrayList<Integer> agesList = ages.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        assertEquals("what are the elements of the list", Arrays.asList(), agesList);
    }
}

class Averager implements IntConsumer {
    private int total = 0;
    private int count = 0;

    public double average() {
        return count > 0 ? ((double) total) / count : 0;
    }

    @Override
    public void accept(final int i) {
        total += i;
        count++;
    }

    public void combine(final Averager other) {
        total += other.total;
        count += other.count;
    }
}