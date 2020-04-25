package shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageAcceptor extends Remote {
    void checkConnection() throws RemoteException;
    void setGameId(String id) throws RemoteException;
    void needUpdate() throws RemoteException;
    void yourAreWin() throws RemoteException;
    void yourAreLose() throws RemoteException;
}
