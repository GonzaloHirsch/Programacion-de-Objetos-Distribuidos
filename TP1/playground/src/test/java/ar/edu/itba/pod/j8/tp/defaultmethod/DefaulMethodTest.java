package ar.edu.itba.pod.j8.tp.defaultmethod;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ar.edu.itba.pod.j8.tp.defaultmethod.AbstractingHelloInterface;
import ar.edu.itba.pod.j8.tp.defaultmethod.BaseHelloInterfaz;
import ar.edu.itba.pod.j8.tp.defaultmethod.ClassInterfaceConflictingHello;
import ar.edu.itba.pod.j8.tp.defaultmethod.ConflictingInterfacesHello;
import ar.edu.itba.pod.j8.tp.defaultmethod.MultipleInheritanceInterfaceHello;
import ar.edu.itba.pod.j8.tp.defaultmethod.OverridingHelloInterface;
import ar.edu.itba.pod.j8.tp.defaultmethod.ReversedMultipleInheritanceInterfaceHello;

/**
 * Test to try the default method inheritance.
 * 
 * This test requires a lot of interfaces and implementations to be able to see the inheritance rules so it is
 * important to check those outs to understand how this works
 *
 * @author Marcelo
 * @since Jul 29, 2015
 */
public class DefaulMethodTest {

    @Test
    public final void direct_default_method() {
        final BaseHelloInterfaz imp = new BaseHelloInterfaz() {
        };

        // TODO SOLUTION: expected value is "Hello Base"
        assertEquals("Hello Base", imp.hello());
    }

    @Test
    public final void overrided_default_method() {
        final OverridingHelloInterface imp = new OverridingHelloInterface() {
        };
        // TODO SOLUTION: expected value is the one set in the overriding interface
        assertEquals("Override Hello", imp.hello());
    }

    @Test
    public final void abracted_default_method() {
        // TODO SOLUTION: this can be improved with a lambda, final AbstractingHelloInterface imp = (() -> "local hello");
        final AbstractingHelloInterface imp = new AbstractingHelloInterface() {

            @Override
            public String hello() {
                return "local hello";
            }

        };
        // TODO SOLUTION: expected value is the one in the anonymous implementation
        assertEquals("local hello", imp.hello());
    }

    @Test
    public final void dependant_interface_conflict_resolution() {
        // TODO SOLUTION: expected value the one for the interface lower in the tree
        assertEquals("Override Hello", new MultipleInheritanceInterfaceHello().hello());
        assertEquals("Override Hello", new ReversedMultipleInheritanceInterfaceHello().hello());
    }

    @Test
    public final void conflict_class_interface_class_wins() {
        // TODO SOLUTION: expected value is the one in the overriding class
        assertEquals("Hello Class", new ClassInterfaceConflictingHello().hello());
    }

    @Test
    public final void conflict_two_interfaces_override_needed() {
        // FIXME go to the class and delete the method implemented to check the error due to the conflict
        // TODO SOLUTION: the error is because the method from one class is defined by the interface, so there are 2 different methods with the same signature
        assertEquals("Override Hello Hello Conflict", new ConflictingInterfacesHello().hello());
    }
}
