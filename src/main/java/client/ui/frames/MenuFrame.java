package client.ui.frames;

import client.listeners.ExitListener;
import client.listeners.StartGameListener;
import client.ui.util.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.config.Config;
import shared.interfaces.Register;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MenuFrame extends JFrame {
    private static Logger LOG = LoggerFactory.getLogger(MenuFrame.class);

    private Registry registry;
    private Register register;
    private JLabel welcomeLabel;
    private JButton startGameButton;
    private JButton exitButton;

    public MenuFrame() {
        setTitle("Sea Battle: Menu");
        setMinimumSize(new Dimension(550, 250));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        add(panel, BorderLayout.CENTER);
        if (createConnection()) {
            welcomeLabel = AppUtils.createLabel("Welcome to game!", panel, new Dimension(500, 50));
            startGameButton = AppUtils.createButton("Start game", panel, new Dimension(500, 50), new StartGameListener(registry, register));
        } else
            welcomeLabel = AppUtils.createLabel("Error: can't create connection", panel, new Dimension(500, 50));
        exitButton = AppUtils.createButton("Exit", panel, new Dimension(500, 50), new ExitListener(this));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setVisible(true);
    }

    private boolean createConnection() {
        try {
            registry = LocateRegistry.getRegistry(Config.REG_HOST, Config.REG_PORT);
            register = (Register) registry.lookup(Config.REG_BIND_NAME);
            LOG.info("Connection was created");
            return true;
        } catch (RemoteException | NotBoundException e) {
            LOG.error("Can't create connection: ", e);
            return false;
        }
    }
}
