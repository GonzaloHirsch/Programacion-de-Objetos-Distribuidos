package client_1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(final String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        final GenericService service = (GenericService) Naming.lookup("//127.0.0.1:1099/service");
        for (int i = 0; i < 4; i++) {
            User user = new User("Name_" + i, "id_" + i);
            service.addUserToServiceQueue(user);
            System.out.println("Added user " + "Name_" + i);
        }
        for (int i = 0; i < 4; i++) {
            User user = service.getFirstUserInServiceQueue();
            System.out.println("Got user " + user.getUsername());
        }
    }
}
