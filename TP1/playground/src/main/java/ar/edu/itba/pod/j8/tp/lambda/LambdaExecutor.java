
package ar.edu.itba.pod.j8.tp.lambda;

import java.io.File;
import java.io.FileFilter;
import java.security.PrivilegedAction;
import java.util.Comparator;
import java.util.concurrent.Callable;

/**
 * Defines methods than can uses lambdas.
 *
 * @author Marcelo
 * @since Jul 30, 2015
 */
public class LambdaExecutor {

    /** @return true if the comparator says 2, 3 are equals parameters are equals (returns 0) */
    public boolean weirdEquals(Comparator<Integer> cp) {
        return cp.compare(3, 2) == 0;
    }

    /**
     * @return a strange comparator that compares the first parameter to the parameter of the method (instead
     *         of the second parameter)
     */
    public Comparator<String> buildWeirdComparator(String one) {
        return (x, y) -> one.compareTo(x);
    }

    /** @return the result of runnning the {@link FileFilter#accept(File)} on root */
    public boolean rootDirectoryComplies(FileFilter fileFilter) {
        return fileFilter.accept(new File("/"));
    }

    /** just runs the runneable */
    public void run(Runnable r) {
        r.run();
    }

    /** @return whatever the {@link Callable} returns */
    public String run(Callable<String> c) throws Exception {
        return c.call();
    }

    /** @return whatever the {@link PrivilegedAction} returns */
    public Integer run(PrivilegedAction<Integer> pa) {
        return pa.run();
    }
}
