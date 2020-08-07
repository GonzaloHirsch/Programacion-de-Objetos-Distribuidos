
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Interface that overrides the {@link BaseHelloInterfaz#hello()} to make it {@link AbstractingHelloInterface}
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public interface AbstractingHelloInterface extends BaseHelloInterfaz {
    @Override
    String hello();
}
