
package ar.edu.itba.pod.j8.tp.model;

import java.time.LocalDate;

/**
 * Person model.
 * 
 * @author Marcelo
 * @since Jul 29, 2015
 */
public class Person {
    public enum Sex {
        MALE, FEMALE
    }

    private String name;
    private LocalDate birthday;
    private Sex gender;
    private String emailAddress;
    private Car car;

    public Person(String name, LocalDate birthday, Sex gender, String emailAddress) {
        super();
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.emailAddress = emailAddress;
    }

    public int getAge() {
        return LocalDate.now().getYear() - birthday.getYear();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    void setCar(Car car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public Sex getGender() {
        return gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Car getCar() {
        return car;
    }

    public static int compareByAge(Person a, Person b) {
        return a.getBirthday().compareTo(b.getBirthday());
    }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return name;
    }
}
