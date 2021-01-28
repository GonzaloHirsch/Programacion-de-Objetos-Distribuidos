package ar.edu.itba.pod.client;

import ar.edu.itba.pod.GenericService;
import ar.edu.itba.pod.User;
import ar.edu.itba.pod.UserAvailableCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static Logger LOG = LoggerFactory.getLogger(Client.class);

    public static void main(final String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        final GenericService service = (GenericService) Naming.lookup("//127.0.0.1:1099/service");

        UserAvailableCallbackHandler handler = new UserAvailableCallbackHandlerImpl();

        UnicastRemoteObject.exportObject(handler, 0);

        tryGetUser(service, handler);

        tryGetUser(service, handler);

        for (int i = 0; i < 4; i++) {
            Thread.sleep(1000);
            User user = new User("Name_" + i, "id_" + i);
            service.addUserToServiceQueue(user);
            LOG.debug("Added user Name_{}", i);
        }

        tryGetUser(service, handler);

        tryGetUser(service, handler);
    }

    public static void tryGetUser(GenericService service, UserAvailableCallbackHandler handler){
        Runnable r = () -> {
            try {
                service.getFirstUserInServiceQueue(handler);
            } catch (RemoteException e) {
                LOG.error("Error getting the user");
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
