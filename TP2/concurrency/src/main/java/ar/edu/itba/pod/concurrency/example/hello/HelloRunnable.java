package ar.edu.itba.pod.concurrency.example.hello;

/**
 * Hello World Example for Threads using {@link Runnable}
 */
public class HelloRunnable implements Runnable {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        Thread helloThread = new Thread(new HelloRunnable());
        helloThread.start();
    }
}
