package ar.edu.itba.pod.concurrency.example.hello;

/**
 * Runs {@link InterruptableRunnable}s to test sleep interrupt and join.
 */
public class SleepyThreads {
    public static void main(final String[] args) throws InterruptedException {
        final Thread[] ts = new Thread[2];
        for (int i = 0; i < ts.length; i++) {
            final Thread thread = new Thread(new InterruptableRunnable(), "sleeper-" + i);
            thread.start();
            ts[i] = thread;
        }
        ts[1].interrupt();
        ts[0].join();
    }
}

/**
 * with {@link Thread#interrupted()}
 */
class InterruptableRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Siesta numero: " + i);
            if (Thread.interrupted()) {
                System.out.println("Interrupted");
                return;
            }
        }

    }
}

/**
 * {@link Thread#sleep(long, int)} with {@link InterruptedException} management.
 */
class SleeperRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Siesta numero: " + i);
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                System.out.println("Interrupted");
                return;
            }

        }
    }
}
