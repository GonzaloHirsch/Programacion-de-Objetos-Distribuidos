package ar.edu.itba.pod;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserAvailableCallbackHandler extends Remote {
    void userAvailable(User user) throws RemoteException;
}
