
package ar.edu.itba.pod.j8.tp.lambda;

import java.util.concurrent.Callable;

/**
 * the lexical scope of the {@link #r1} lambda
 * 
 * @author Marcelo
 * @since Jul 29, 2015
 */
public class LambdaScopingHelloWorld {
    Callable<String> r1 = () -> {
        // if we were using inner classes this would be the inner class
        return this.toString();
    };

    public String toString() {
        return "Hello, world!";
    }
}
