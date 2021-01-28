package custom_client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CustomService extends Remote {

    String ping() throws RemoteException;

    long time() throws RemoteException;

    String echo(String message) throws RemoteException;

    String hello(String name) throws RemoteException;

    String fortune() throws RemoteException;
}
