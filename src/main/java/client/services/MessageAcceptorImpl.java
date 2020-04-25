package client.services;

import client.ui.frames.GameFrame;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.interfaces.MessageAcceptor;

import java.rmi.RemoteException;

@AllArgsConstructor
public class MessageAcceptorImpl implements MessageAcceptor {

    private static Logger LOG = LoggerFactory.getLogger(MessageAcceptorImpl.class);
    private GameFrame gameFrame;

    @Override
    public void checkConnection() throws RemoteException {
        LOG.info("Check connection");
    }

    @Override
    public void setGameId(String id) throws RemoteException {
        LOG.info("Get game id: {}", id);
        gameFrame.startGame(id);
    }

    @Override
    public void needUpdate() throws RemoteException {
        LOG.info("Need update");
        gameFrame.update();
    }

    @Override
    public void yourAreWin() throws RemoteException {
        LOG.info("Return win message");
        gameFrame.setWin();
    }

    @Override
    public void yourAreLose() throws RemoteException {
        LOG.info("Return lose message");
        gameFrame.setLose();
    }
}
