package ar.edu.itba.pod.j8.tp.exercise_2;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // Populating the collection
        Collection<Country> countries = Arrays.asList(
                new Country("Argentina", Continent.AMERICA, 43590400,
                        LocalDate.parse("1816-07-09")),
                new Country("China", Continent.ASIA, 1377939144, null),
                new Country("Brazil", Continent.AMERICA, 206284591,
                        LocalDate.parse("1822-09-07")),
                new Country("Nigeria", Continent.AFRICA, 189636000,
                        LocalDate.parse("1960-10-01")),
                new Country("Japón", Continent.ASIA, 126675000, null),
                new Country("México", Continent.AMERICA, 122273000,
                        LocalDate.parse("1810-09-16")),
                new Country("Turquía", Continent.ASIA, 79265000, LocalDate.parse("1923-10-29")),
                new Country("Australia", Continent.OCEANIA, 24117000, null),
                new Country("Chile", Continent.AMERICA, 18192000,
                        LocalDate.parse("1810-09-18")),
                new Country("Bélgica", Continent.EUROPE, 11338000,
                        LocalDate.parse("1930-10-04")),
                new Country("Portugal", Continent.EUROPE, 10262000,
                        LocalDate.parse("1640-12-01")),
                new Country("Bolivia", Continent.AMERICA, 10985000,
                        LocalDate.parse("1825-08-06")),
                new Country("Suecia", Continent.EUROPE, 9824000, LocalDate.parse("1523-06-06")),
                new Country("Israel", Continent.ASIA, 8391000, LocalDate.parse("1948-05-15")),
                new Country("Inglaterra", Continent.EUROPE, 53010000, null),
                new Country("Serbia", Continent.EUROPE, 7071000, LocalDate.parse("1804-02-15"))
        );

        // Creating a stream supplier to avoid closed stream errors
        Supplier<Stream<Country>> streamSupplier = countries::stream;

        System.out.println("1. Imprimir en pantalla todos los países");
        printAllCountries(streamSupplier.get());
        System.out.println("2. Imprimir en pantalla a los países de América");
        printAllAmericanCountries(streamSupplier.get());
        System.out.println("3. Crear una lista con los países de América");
        generateAllAmericanList(streamSupplier.get());
        System.out.println("NO PRINTABLE RESULT");
        System.out.println("4. Crear un set con los países que contengan en su nombre la letra 'a'");
        generateSetOfCountriesWithA(streamSupplier.get());
        System.out.println("5. Crear una lista con los nombres de los países que contengan en su nombre la letra 'a'");
        generateListOfCountriesWithA(streamSupplier.get());
        System.out.println("6. Crear un mapa (K,V) con K: Continente y V: Lista de países");
        generateContinentCountryListMap(streamSupplier.get());
        System.out.println("7. Crear un mapa (K,V) con K: Continente y V: Total de habitantes");
        generateContinentInhabitants(streamSupplier.get());
        System.out.println("8. Crear un mapa (K,V) con K: Continente y V: Promedio de habitantes");
        generateContinentInhabitantsAverage(streamSupplier.get());
        System.out.println("9. Obtener un país que tenga una 'a' en su nombre y la cantidad de habitantes sea mayor a 1.000.000.000. En caso de no encontrar uno, devolver null");
        getCountryWithAAndMillionInhabitants(streamSupplier.get());
        System.out.println("10. Obtener el país con el mayor número de habitantes");
        getBiggestCountry(streamSupplier.get());
        System.out.println("11. Listar todos los países cuya independencia fue en un año bisiesto");
        getAllLeapYearCountries(streamSupplier.get());
        System.out.println("12. Armar un mapa que muestre la cantidad de países que se independizaron por siglo. Donde el siglo se puede escribir como 1800 o 19 (para toda independencia ocurrida en un año 18XX)");
        getIndependenceMap(streamSupplier.get());
    }

    public static void printAllCountries(Stream<Country> s) {
        s.forEach(System.out::println);
    }

    public static void printAllAmericanCountries(Stream<Country> s) {
        s.filter(x -> x.getContinent() == Continent.AMERICA).forEach(x -> System.out.println(x.getName()));
    }

    public static void generateAllAmericanList(Stream<Country> s) {
        s.filter(x -> x.getContinent() == Continent.AMERICA).collect(Collectors.toList());
    }

    public static void generateSetOfCountriesWithA(Stream<Country> s) {
        Set<Country> set = s.filter(x -> x.getName().contains("a") || x.getName().contains("A")).collect(Collectors.toSet());
        set.forEach(System.out::println);
    }

    public static void generateListOfCountriesWithA(Stream<Country> s) {
        List<Country> set = s.filter(x -> x.getName().contains("a") || x.getName().contains("A")).collect(Collectors.toList());
        set.forEach(System.out::println);
    }

    public static void generateContinentCountryListMap(Stream<Country> s) {
        Map<Continent, List<Country>> data = s.collect(Collectors.groupingBy(Country::getContinent));
        data.forEach((k, v) -> {
            System.out.println("------");
            System.out.println(k.name());
            v.forEach(System.out::println);
            System.out.println("------");
        });
    }

    public static void generateContinentInhabitants(Stream<Country> s) {
        Map<Continent, Long> data = s.collect(Collectors.groupingBy(Country::getContinent, Collectors.summingLong(Country::getPopulation)));
        data.forEach((k, v) -> {
            System.out.println("------");
            System.out.println(k.name() + " -> " + v);
            System.out.println("------");
        });
    }

    public static void generateContinentInhabitantsAverage(Stream<Country> s) {
        Map<Continent, Double> data = s.collect(Collectors.groupingBy(Country::getContinent, Collectors.averagingLong(Country::getPopulation)));
        data.forEach((k, v) -> {
            System.out.println("------");
            System.out.println(k.name() + " -> " + v);
            System.out.println("------");
        });
    }

    public static void getCountryWithAAndMillionInhabitants(Stream<Country> s) {
        Optional<Country> maybeCountry = s.filter(x -> x.getName().contains("a") || x.getName().contains("A")).filter(x -> x.getPopulation() >= 1000000000).findAny();
        System.out.println(maybeCountry.orElse(null));
    }

    public static void getBiggestCountry(Stream<Country> s) {
        Optional<Country> maybeCountry = s.max(Comparator.comparingLong(Country::getPopulation));
        System.out.println(maybeCountry.orElse(null));
    }

    public static void getAllLeapYearCountries(Stream<Country> s) {
        s.filter(x -> x.getIndependenceDay().isPresent() && x.getIndependenceDay().get().isLeapYear()).forEach(System.out::println);
    }

    public static void getIndependenceMap(Stream<Country> s){
        Map<Integer, List<Country>> data = s.filter(x -> x.getIndependenceDay().isPresent()).collect(Collectors.groupingBy(c -> c.getIndependenceDay().get().getYear() / 100));
        data.forEach((k, v) -> {
            System.out.println("------");
            System.out.println("CENTURY - " + (k * 100));
            v.forEach(System.out::println);
            System.out.println("------");
        });
    }
}
