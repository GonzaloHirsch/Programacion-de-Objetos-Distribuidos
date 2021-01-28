# Java 8
[Go to Index](resumen.md)

## Try With Resources

**Try with Resources** es para evitar el *null-check* en el finally de un try-catch, los recursos se cierran solos en el finally, si es necesario:
```java
public final String readFirstLineFromFile(String path) throws IOException
{
    try (BufferedReader br = new BufferedReader(new FileReader(path))){
        return br.readLine();
    }
}
```

## Functional Interface

Son interfaces que tienen **1 solo método abstracto para implementar**, y se puede agregar `@FunctionalInterface` para que el compilador verifique que cumple lo anterior.
:
```java
@FunctionalInterface
public interface MyFunctionalInterface<T> {
    boolean apply(T t);
}
```

Algunos ejemplos de interfaces funcionales son:
 - `Predicate<T>` --> Boolean-valued property of an object
 - `Consumer<T>` --> Action performed on an object
 - `Function<T, R>` --> Function transforming T into R
 - `Supplier<T>` --> provide instance of T
 - `UnaryOperator<T>` --> Function from T to T
 - `BinaryOperator<T>` --> Function from (T,T) to T

Estas interfaces pueden tener métodos **estáticos** o **default** que sirve como utility methods.

## Default Methods

Son métodos con cuerpo dentro de las interfaces para definir comportamiento predefinido.

Cuando una interfaz extiende a otra con un método default:
 1. Puede no mencionar al método, entonces queda el definido por la interfaz padre
    ```java
    public interface LazyComparator<T> extends Comparator<T> {}
    ```
 2. Puede mencionarlo pero sin default, que lo convierte en abstracto
    ```java
    public interface AbstractComparator<T> extends Comparator<T> {
        Comparator<T> reversed();
    }
    ```
 3. Puede mencionarlo con default, lo que estaría overrideando al método
    ```java
    public interface OverrideComparator<T> extends Comparator<T> {
        default Comparator<T> reversed() {return this; }
    }
    ```

Que pasa cuando dos clases definen el mismo método default:
 1. Si son clase vs interfaz, se toma el de la clase
 2. Si una de las interfaces hereda de la otra, se toma el de la interfaz hija
 3. Si son 2 interfaces independientes, la clase que las implementa debe overridear el método

## Method Reference

La referencia se hace con `::`, y puede ser:
 - **Método Estático** --> ClassName::methodName (`Integer::sum`)
 - **Método de Instancia** sobre instancia específica --> instanceRef::methodName (`mySet::contains`)
 - **Método de Instancia** sobre objeto no instanciado --> ClassName::methodName (`Class::new`)

## Type Inference

Java puede deducir el tipo genérico a partir del principio de la declaración:
```java
// Java 7
Map<String, Map<String, String>> mapOfMaps = new HashMap <String, Map<String, String>> ();
// Java 8+
Map<String, Map<String, String>> mapOfMaps = new HashMap<>();
```

## Lambdas

Expresan una instancia de una interfaz funcional, permiten código más simple y legible, pasa de:
```java
EventQueue.invokeLater(
    new Runnable() {
        @Override
        public void run() {
            System.out.println("Running");
        }
    }
);
```
a
```java
EventQueue.invokeLater(() -> System.out.println("Running"));
```

La sintaxis es:  
```java
// Parametros
(String x, int y) -> {
    // Cuerpo
    System.out.println("recibi" + x);
    return y + 1;
}
```

Aunque tiene algunos *Syntax Sugars*:
 - No hace falta el return --> `(x, y) -> x + y`
 - Con 1 parámetro no hacen falta paréntesis --> `x -> "hola"`
 - Si no recibe parámetros --> `() -> 42`
 - Void lambda --> `(String s) -> { System.out.println(s);}`

Las lambdas también pueden usar **type inference** y **target typing**, en donde se infieren (si se puede) los tipos a partir de en donde se asigna y como.

**Lexical Scoping**, como no son *inner classes*, tienen el scope de quién las contiene.

**Effectively Final**, son variables que no están declaradas como finales, pero como no se modifican, se las toma como final. Dentro de una lambda, si uso una variable de quién la contiene, debe ser *final* o *effectively final*, y las variables dentro de las lambda no puede hacen *shadowing* de las de quién la contiene.

Por la captura de tipo y otras variables, reduce los memory leaks.

## Optional

El `optional` es un contenedor que puede o no tener un valor. Además se puede preguntar si existe un valor, y también generar comportamiento para si existe o no.

Para generar un `optional`:
```java
Optional<String> opt1 = Optional.empty();
Optional<Integer> opt2 = Optional.ofNullable(values.get("key"));
Optional<Integer> opt3 = Optional.of(values.get("key")); // Potenicial null pointer
```

Para usar los condicionales:
```java
// Long version
Optional<Soundcard> soundcard = ...
if(soundcard.isPresent()){
    System.out.println(soundcard.get());
}

// Method reference version
soundcard.ifPresent(System.out::println);

// Si no hay elemento
soundcard.orElse(otherSoundcard);
soundcard.orElseGet(() -> createSoundCard())
soundcard.orElseThrow(IllegalStateException::new)
```

## Collections

Se pueden convertir las colecciones en streams, y ahi se pueden usar lambdas para transformar a la información. Los métodos son:
```java
default Stream<E> stream()
default Stream<E> parallelStream()
default Spliterator<E> spliterator()
```

La sintaxis para usar streams es:
 1. Una **fuente** --> Colección o generador al que se le pueda aplicar el método `stream()`.
 2. Una **cadena de operaciones intermedias** --> Cada una genera un nuevo stream para poder aplicarle más operaciones.
 3. Una **operación terminal** --> No genera un nuevo stream, sino una nueva collección o valor.

Hay varias formas de construir un `Stream`, algunas son:
 - A partir de una colección con el método `stream()`
 - Usando los métodos estáticos de `Stream` como `::of(T…)`, `::empty()`, `::concat(Stream, Stream)`, `::generate(Supplier)` o `::iterate(seed, UnaryOperator)`
 - Usando el builder `Stream.Builder`

Las **operaciones intermedias** generan nuevos streams, y son *lazy*, lo que significa que no se calculan hasta que una operación terminal las requiere. Algunas operaciones son:
 - **map** --> Mapea cada elemento del stream con una función T->W
 - **flatMap** --> Aplica una función T->Stream(W) a cada elemento y aplana los
resultados.
 - **limit** --> limita la cantidad de elementos del stream
 - **distinct** --> genera un stream sin elementos repetidos según el `equals()`
 - **filter** --> aplica un predicado a cada elemento dejando solo los que cumplen.
 - **sorted** --> ordena los elementos de acuerdo a un Comparator

Las **operaciones terminales** generan una nueva colección o un valor, y al aplicarse se cierra el Stream automáticamente.

### Aggregators

El **reduce** aplica la función `accumulator` entre lo acumulado y el próximo valor del stream, el `identity` es el valor inicial. La última versión es solo para parallel streams, y aplica el `combiner` entre resultados parciales:
```java
reduce(BinaryOperator<T> accumulator)
reduce(T identity, BinaryOperator<T> accumulator)
reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U>combiner)
```

El **collect** permite modificar/mutar los valores para obtener un valor final o colección de valores:
```java
collect(Collector<? super T,A,R> collector)
collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)
```

Los **collectors** reciben los siguiente, aunque hay algunos ya definidos:
 - `Supplier<A> supplier` --> para obtener la clase que acumulará
 - `BiConsumer<A,T> accumulator` --> función de la clase acumulador para "acumular"
 - `BinaryOperator<A> combiner` --> cómo combinar resultado de acumulaciones paralelas
 - `Function<A, R> finisher` --> una función para aplicar sobre el resultado final (típicamente Identidad)
 - `Characteristics... characteristics`--> valores que permiten optimizar las transformaciones.

## Java Time

La nueva API de **Java Time** cumple con los siguientes principios:
 - *Clara* --> métodos bien definidos y comportamiento esperable
 - *Fluent* --> fácil de leer
 - *Immutable* --> las operaciones generan nuevos objetos en vez de modificarlos

Viene con *factory methods*:
 - **of** --> a partir de parámetros
 - **from** --> a partir de convertir los parámetros
 - **parse** --> a partir de un string

```java
//Current Date
LocalDate today = LocalDate.now();
//Creating LocalDate with values
LocalDate firstDay_2014 = LocalDate.of(2014, Month.JANUARY, 1);
LocalDate.ofEpochDay(365);
LocalDate.ofYearDay(2014, 100);
LocalDate localDate = LocalDate.from(Instant.now());
//parse
LocalDateTime dt = LocalDateTime.parse("27::Apr::2014 21::39::48", DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"));
```

Los objetos pueden ser *tipos de momento* (X es localización):
 - **XDate** --> fecha
 - **XTime** --> hora
 - **XDateTime** --> fecha y hora

También pueden ser *localización*:
 - **LocalX** --> sin timezone
 - **ZonedX** --> con timezone
 - **ZoneID/ZoneOffset** --> para settear zonas

También existen **period** que son rangos con precisión al día, o **duration** que son rangos con precisón al nano segundo