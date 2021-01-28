package custom_client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.Instant;

public class CustomClient {
    public static void main(final String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        final CustomService service = (CustomService) Naming.lookup("//127.0.0.1:1099/custom");

        System.out.println("I GOT " + service.ping());

        System.out.println("I GOT " + service.echo("MY ECHO MESSAGE"));

        System.out.println("I GOT " + service.hello("GONZA"));

        System.out.println("THE TIME " + Instant.ofEpochMilli(service.time()));
        Thread.sleep(3000);
        System.out.println("THE TIME " + Instant.ofEpochMilli(service.time()));

        for (int i = 0; i < 15; i++){
            System.out.println("FORTUNE IS " + service.fortune());
        }
    }
}
