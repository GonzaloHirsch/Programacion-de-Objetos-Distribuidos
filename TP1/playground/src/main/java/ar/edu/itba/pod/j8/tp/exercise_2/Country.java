package ar.edu.itba.pod.j8.tp.exercise_2;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.time.LocalDate;
import java.util.Optional;

public class Country {
    private final String name;
    private final Continent continent;
    private final Long population;
    private final Optional<LocalDate> independenceDay;

    public Country(final String name, final Continent continent, final long population,
                   final LocalDate independenceDay) {
        this.name = requireNonNull(name);
        this.continent = requireNonNull(continent);
        this.population = population;
        this.independenceDay = ofNullable(independenceDay);
    }

    public Continent getContinent() {
        return continent;
    }

    public Long getPopulation() {
        return population;
    }

    public String getName() {
        return name;
    }

    public Optional<LocalDate> getIndependenceDay() {
        return independenceDay;
    }

    @Override
    public String toString() {
        return name + " (" + population + " hab)";
    }
}
