
package ar.edu.itba.pod.j8.tp.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.pod.j8.tp.model.Car;
import ar.edu.itba.pod.j8.tp.model.Person;
import ar.edu.itba.pod.j8.tp.model.Car.Type;
import ar.edu.itba.pod.j8.tp.model.Person.Sex;

/**
 * Class to play with streams... the idea is to "pipelineize" the non java 8 actions of each test
 *
 * @author Marcelo
 * @since Aug 6, 2015
 */
public class StreamIntegrationTest {
    private List<Person> roster;

    @Before
    public final void before() {

        roster = Arrays.asList(new Person("jack", LocalDate.of(1999, 1, 1), Sex.MALE, "j@mail.com"),
                new Person("danielle", LocalDate.of(1992, 12, 1), Sex.FEMALE, "d@mail.com"), new Person(
                        "livy", LocalDate.of(1989, 5, 12), Sex.FEMALE, "l@mail.com"), new Person("mark",
                        LocalDate.of(1993, 1, 10), Sex.MALE, "m@mail.com"),
                new Person("anna", LocalDate.of(1985, 3, 11), Sex.FEMALE, "a@mail.com"), new Person("bree",
                        LocalDate.of(1985, 5, 6), Sex.FEMALE, "b@mail.com"));
        new Car(roster.get(0), Type.TOWNCAR, "i-1");
        new Car(roster.get(1), Type.TOWNCAR, "i-2");
        new Car(roster.get(2), Type.PICKUP, "i-3");
        new Car(roster.get(3), Type.PICKUP, "i-4");
        new Car(roster.get(4), Type.PICKUP);
    }

    @Test
    public final void should_get_only_males() {
        final List<String> names = new ArrayList<>();
        //turn into java 8 stream way
        for (final Person p : roster) {
            if (p.getGender() == Person.Sex.MALE) {
                names.add(p.getName());
            }
        }
        fail("turn the grouping into java 8 and then remove this line! (and of courese fix the assertions)");
        assertEquals(Arrays.asList("jack"), names);
    }

    @Test
    public final void should_count_by_type() {
        final Map<Car.Type, Integer> countByType = new HashMap<>();
        for (final Person p : roster) {
            if (p.getAge() > 18) {
                final Car car = p.getCar();
                if (car != null && car.getInsuranceId() != null) {
                    final Integer count = countByType.getOrDefault(car.getType(), 1);
                    countByType.put(car.getType(), count + 1);
                }
            }
        }
        fail("turn the grouping into java 8 and then remove this line! (and of courese fix the assertions)");
        assertEquals(new Integer(0), countByType.get(Type.TOWNCAR));
        assertEquals(new Integer(0), countByType.get(Type.PICKUP));
    }
}
