
package ar.edu.itba.pod.j8.tp.defaultmethod;

/**
 * Class with conflicting default methods but as one is a class {@link ImplementingAbstractClass} and the
 * other is an interface {@link BaseHelloInterfaz} no problem arises as class wins (eventhough they are not
 * related
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public class ClassInterfaceConflictingHello extends ImplementingAbstractClass implements BaseHelloInterfaz {

}
