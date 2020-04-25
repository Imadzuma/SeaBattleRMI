package server.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.data.Game;
import server.services.RegisterImpl;
import shared.config.Config;
import shared.interfaces.GameAcceptor;
import shared.interfaces.MessageAcceptor;
import shared.interfaces.Register;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameManager extends Thread {

    private static Logger LOG = LoggerFactory.getLogger(GameManager.class);

    private RegisterImpl register;
    private Registry registry;
    private List<String> players;
    private Game game = null;
    GameAcceptor gameAcceptor1;
    GameAcceptor gameAcceptor2;

    public GameManager(String name) {
        super(name);
        players = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            if (Config.REG_HOST.equals("localhost"))
                registry = LocateRegistry.createRegistry(Config.REG_PORT);
            else
                registry = LocateRegistry.getRegistry(Config.REG_HOST, Config.REG_PORT);
            register = new RegisterImpl(this);
            Register stub = (Register) UnicastRemoteObject.exportObject(register, 0);
            registry.rebind(Config.REG_BIND_NAME, stub);
            LOG.info("Regist register: {}", Config.REG_BIND_NAME);
            LOG.info("Game Manager started");
        } catch (RemoteException e) {
            LOG.error("Can't start game manager, try create local registry", e);
        }
    }

    public synchronized void addPlayer(String id) {
        LOG.info("Add player: {}", id);
        players.add(id);
        createGame();
    }

    private synchronized void createGame() {
        if (game != null)
            return;
        while (players.size() >= 2) {
            String firstPlayer = players.get(0);
            String secondPlayer = players.get(1);
            MessageAcceptor messageAcceptor1, messageAcceptor2;
            try {
                messageAcceptor1 = (MessageAcceptor)registry.lookup(Config.ACCEPTOR_PREFIX + firstPlayer);
                messageAcceptor1.checkConnection();
            } catch (RemoteException | NotBoundException e) {
                LOG.warn("Can't connect to client", e);
                players.remove(0);
                continue;
            }
            try {
                messageAcceptor2 = (MessageAcceptor)registry.lookup(Config.ACCEPTOR_PREFIX + secondPlayer);
                messageAcceptor2.checkConnection();
            } catch (RemoteException | NotBoundException e) {
                LOG.warn("Can't connect to player", e);
                players.remove(1);
                continue;
            }
            String gameId = UUID.randomUUID().toString();
            players.remove(0);
            players.remove(0);
            game = new Game(registry, gameId, firstPlayer, secondPlayer, messageAcceptor1, messageAcceptor2, this);
        }
    }

    public void closeGame() {
        game = null;
    }

}
