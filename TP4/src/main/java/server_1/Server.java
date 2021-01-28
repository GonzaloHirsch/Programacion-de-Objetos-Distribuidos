package server_1;

import client_1.GenericService;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(final String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("rmi-definitions Server Starting ...");
        final GenericService gs = new GenericServiceImpl();
        final Remote remote = UnicastRemoteObject.exportObject(gs, 0);

        final Registry registry = LocateRegistry.getRegistry();
        registry.bind("service", remote);
        System.out.println("Service bound");
    }
}
