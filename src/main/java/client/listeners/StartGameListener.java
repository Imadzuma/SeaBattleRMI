package client.listeners;

import client.ui.frames.GameFrame;
import client.services.MessageAcceptorImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.config.Config;
import shared.interfaces.MessageAcceptor;
import shared.interfaces.Register;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@AllArgsConstructor
public class StartGameListener implements ActionListener {

    private static Logger LOG = LoggerFactory.getLogger(StartGameListener.class);

    private Registry registry;
    private Register register;

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            String clientId = register.generateId();
            LOG.info("Get client id: {}", clientId);
            GameFrame gameFrame = new GameFrame(clientId, registry);
            MessageAcceptorImpl messageAcceptor = gameFrame.getMessageAcceptor();
            MessageAcceptor stub = (MessageAcceptor) UnicastRemoteObject.exportObject(messageAcceptor, 0);
            registry.bind(Config.ACCEPTOR_PREFIX + clientId, stub);
            LOG.info("Regist message acceptor: {}", Config.ACCEPTOR_PREFIX + clientId);
            register.addPlayer(clientId);
        } catch (RemoteException | AlreadyBoundException e) {
            LOG.error("Can't register on game: ", e);
        } catch (IOException e) {
            LOG.error("Can't create game frame: ", e);
        }
    }
}
