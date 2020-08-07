
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Class that implements hello and it is not related to {@link BaseHelloInterfaz} (so a children can conflict
 * and resolve)
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public abstract class ImplementingAbstractClass {
    public String hello() {
        return "Hello Class";
    }
}
