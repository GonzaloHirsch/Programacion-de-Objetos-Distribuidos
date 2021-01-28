package custom_server;

import custom_client.CustomService;
import remote_client.GenericService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CustomServer {
    public static void main(final String[] args) throws RemoteException {
        System.out.println("rmi-definitions Server Starting ...");
        final CustomService gs = new CustomServiceImpl();
        final Remote remote = UnicastRemoteObject.exportObject(gs, 0);

        final Registry registry = LocateRegistry.getRegistry();
        registry.rebind("custom", remote);
        System.out.println("Service bound");
    }
}
