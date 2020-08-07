package ar.edu.itba.pod.j8.tp.optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ar.edu.itba.pod.j8.tp.model.Car;
import ar.edu.itba.pod.j8.tp.model.Person;
import ar.edu.itba.pod.j8.tp.model.Car.Type;
import ar.edu.itba.pod.j8.tp.model.Person.Sex;
import org.junit.rules.ExpectedException;

/**
 * Test to check the {@link Optional} functionality
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public class OptionalTest {
    private Car car;
    private Person person;
    private Person otherPerson;
    private Optional<Person> emptyOptional;
    private Optional<Person> nonEmpty;

    @Rule
    // TODO SOLUTION: added rule to be able to expect exceptions
    public ExpectedException expected = ExpectedException.none();

    @Before
    public final void before() {
        person = new Person("mik", LocalDate.of(2015, 9, 13), Sex.MALE, "mick@persons.com");
        car = new Car(person, Type.PICKUP);
        otherPerson = new Person("ana", LocalDate.of(2015, 9, 13), Sex.FEMALE, "ana@persons.com");
        nonEmpty = Optional.of(person);
        emptyOptional = Optional.empty(); // without the improved type inference this should be
                                          // Optional.<Person>empty()
    }

    @Test
    public final void old_school_conditional() {
        // TODO SOLUTION: commented the assert because the get() on an empty() optional throws an exception
        if (!emptyOptional.isPresent()) {
            //assertEquals(null, emptyOptional.get()); // shouldn't be here
        }

        // TODO SOLUTION: changed expected null for the expected person
        if (nonEmpty.isPresent()) {
            assertEquals(person, nonEmpty.get()); // get the value
        }
    }

    @Test
    public final void new_school_conditional() {
        emptyOptional.ifPresent(p -> fail("should not reach here'")); // is like it es
        // TODO SOLUTION: changed null -> p
        nonEmpty.ifPresent(p -> assertEquals(person, p));// get the value
    }

    @Test
    public final void orElse() {
        // TODO SOLUTION: expected to person
        assertEquals(person, nonEmpty.orElse(otherPerson));
        // TODO SOLUTION: expected to otherPerson
        assertEquals(otherPerson, emptyOptional.orElse(otherPerson));
    }

    @Test
    public final void orElseGet() {
        // TODO SOLUTION: expected person
        assertEquals(person, nonEmpty.orElseGet(() -> otherPerson));
        // TODO SOLUTION: expected otherPerson
        assertEquals(otherPerson, emptyOptional.orElseGet(() -> otherPerson));
    }

    @Test()
    public final void or_throw() {
        // full doesn't go to the or throw returns the value
        assertEquals(person, nonEmpty.orElseThrow(IllegalStateException::new));
        // empty throws (so what should the test "expect"
        // TODO SOLUTION: added the @Rule, that catches the exception if thrown after the expect() method
        expected.expect(IllegalStateException.class);
        emptyOptional.orElseThrow(IllegalStateException::new);
        fail("shouldn't get here");
    }

    @Test
    public final void map() {
        // turns an Optional<T> into an Optional<U> (internally as mapper returns U)
        emptyOptional.map(Person::getCar).ifPresent(c -> fail("shouldn't have a car"));// ugly to break this..
        // TODO SOLUTION: changed expected null -> car
        nonEmpty.map(Person::getCar).ifPresent(c -> assertEquals(car, c));
    }

    @Test
    public final void flatmap() {
        // TODO NOTE: no error in this test
        // turns an Optional<T> into an Optional<U> the mapper needs to return Optional<U>
        nonEmpty.flatMap(p -> Optional.ofNullable(p.getCar())).ifPresent(c -> assertEquals(car, c));
        emptyOptional.flatMap(p -> Optional.ofNullable(p.getCar())).ifPresent(
                c -> fail("shouldn't have a car"));// ugly to break this..
    }

    @Test()
    public final void filter() {
        // if we leave the condition what this test should expect?
        // TODO SOLUTION: the nonEmpty person has a car, so the filter would return an empty optional, that would trigger the first exception
        expected.expect(IllegalArgumentException.class);
        nonEmpty.filter(p -> p.getCar() == null).orElseThrow(IllegalArgumentException::new);
        nonEmpty.filter(p -> p.getCar() != null).orElseThrow(IllegalArgumentException::new);
    }

    @Test()
    public final void get_throws_on_empty() {
        // TODO SOLUTION: we expect a NoSuchElementException
        expected.expect(NoSuchElementException.class);
        emptyOptional.get(); // what should the test expect?
    }

    @Test(expected = NullPointerException.class)
    public final void of_throws_null() {
        // TODO NOTE: no error in this test, there is an exception thrown, but the expected catches it
        Optional.of(null);
    }
}
