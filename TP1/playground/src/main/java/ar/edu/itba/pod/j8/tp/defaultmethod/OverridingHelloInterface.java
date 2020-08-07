
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Interfaz that overrides the default method
 *
 * @author Marcelo
 * @since Jul 29, 2015
 */
public interface OverridingHelloInterface extends BaseHelloInterfaz {
    @Override
    default String hello() {
        return "Override Hello";
    }
}
