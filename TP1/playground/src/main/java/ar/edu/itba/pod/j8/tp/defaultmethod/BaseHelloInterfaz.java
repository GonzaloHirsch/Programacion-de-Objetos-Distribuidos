
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Base interfaz to test default method.
 *
 * @author Marcelo
 * @since Jul 29, 2015
 */
public interface BaseHelloInterfaz {
    default String hello() {
        return "Hello Base";
    }
}
