package shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Register extends Remote {
    String generateId() throws RemoteException;
    void addPlayer(String id) throws RemoteException;
}
