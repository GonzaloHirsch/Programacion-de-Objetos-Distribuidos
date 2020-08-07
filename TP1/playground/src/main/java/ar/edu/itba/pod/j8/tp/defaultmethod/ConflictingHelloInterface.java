
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Conflicting default interface
 *
 * @author Marcelo
 * @since Jul 29, 2015
 */
public interface ConflictingHelloInterface {
    default String hello() {
        return "Hello Conflict";
    }
}
