package server.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.threads.GameManager;
import shared.interfaces.Register;

import java.rmi.RemoteException;
import java.util.UUID;

@AllArgsConstructor
public class RegisterImpl implements Register {

    private static Logger LOG = LoggerFactory.getLogger(RegisterImpl.class);

    private GameManager gameManager;

    public String generateId() throws RemoteException {
        LOG.info("Player ask id");
        return UUID.randomUUID().toString();
    }

    @Override
    public void addPlayer(String id) throws RemoteException {
        LOG.info("Player adds on game");
        gameManager.addPlayer(id);
    }

}
