
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Class that implements 2 interfaces that implement hello and conflict, so there is no remedy but to override
 * to solve
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public class ConflictingInterfacesHello implements OverridingHelloInterface, ConflictingHelloInterface {
    @Override
    public String hello() {
        return OverridingHelloInterface.super.hello() + " " + ConflictingHelloInterface.super.hello();
    }
}
