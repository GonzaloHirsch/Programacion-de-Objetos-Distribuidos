package remote_client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemoteClient {
    public static void main(final String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        final GenericService service = (GenericService) Naming.lookup("//127.0.0.1:1099/service");
        for (int i = 0; i < 4; i++) {
            service.addVisit();
        }
        while (true){
            Thread.sleep(1000);
            service.addVisit();
            System.out.println("visits " + service.getVisitCount());
        }
    }
}
