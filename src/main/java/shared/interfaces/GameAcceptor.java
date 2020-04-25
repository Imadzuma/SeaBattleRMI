package shared.interfaces;

import shared.data.PlayerBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameAcceptor extends Remote {
    Boolean isMyTurn() throws RemoteException;
    PlayerBoard getMyBoard() throws RemoteException;
    PlayerBoard getOpponentBoard() throws RemoteException;
    boolean getTurn(int x, int y) throws RemoteException;
}
