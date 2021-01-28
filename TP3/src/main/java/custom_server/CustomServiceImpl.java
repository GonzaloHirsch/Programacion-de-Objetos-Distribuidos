package custom_server;

import custom_client.CustomService;
import remote_client.GenericService;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;

/**
 * Basic implementation of {@link ar.edu.itba.pod.concurrency.e1.GenericService}.
 */
public class CustomServiceImpl implements CustomService {

    private final String[] FORTUNES;
    private static final int FORTUNE_COUNT = 10;
    private static final Random r = new Random();

    public CustomServiceImpl(){
        this.FORTUNES = new String[FORTUNE_COUNT];
        this.FORTUNES[0] = "About time i got out of that cookie";
        this.FORTUNES[1] = "Run";
        this.FORTUNES[2] = "Better be happy than wise";
        this.FORTUNES[3] = "Happiness is right around the corner, sex is just around back";
        this.FORTUNES[4] = "Your hear will skip a beat";
        this.FORTUNES[5] = "Ignore previous cookie";
        this.FORTUNES[6] = "Romance is waiting for you";
        this.FORTUNES[7] = "I cannot help you, I'm just a cookie";
        this.FORTUNES[8] = "Help me! I'm being held as a prisoner";
        this.FORTUNES[9] = "In youth and beauty, wisdom is rare";
    }


    @Override
    public String ping() throws RemoteException {
        return "pong";
    }

    @Override
    public long time() throws RemoteException {
        return Instant.now().toEpochMilli();
    }

    @Override
    public String echo(String message) throws RemoteException {
        return message;
    }

    @Override
    public String hello(String name) throws RemoteException {
        return "Hello " + name;
    }

    @Override
    public String fortune() throws RemoteException {
        return this.FORTUNES[r.nextInt(FORTUNE_COUNT) % FORTUNE_COUNT];
    }
}
