package client.ui.frames;

import client.services.MessageAcceptorImpl;
import client.ui.panels.Board;
import client.ui.util.AppUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.config.Config;
import shared.data.PlayerBoard;
import shared.interfaces.GameAcceptor;
import shared.interfaces.MessageAcceptor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

@AllArgsConstructor
@Getter
public class GameFrame extends JFrame {
    private static Logger LOG = LoggerFactory.getLogger(GameFrame.class);

    private MessageAcceptorImpl messageAcceptor = new MessageAcceptorImpl(this);
    private GameAcceptor gameAcceptor;
    private Registry registry;
    private String playerId;

    private JLabel infoLabel;
    private Board playerBoard;
    private Board opponentBoard;

    public GameFrame(String playerId, Registry registry) throws IOException {
        this.playerId = playerId;
        this.registry = registry;
        setTitle("Sea Battle: Game");
        setMinimumSize(new Dimension(1020, 520));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel infoPanel = new JPanel();
        infoLabel = AppUtils.createLabel("Wait second player", infoPanel, new Dimension(500, 50));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));
        playerBoard = new Board();
        opponentBoard = new Board();
        mainPanel.add(playerBoard);
        mainPanel.add(opponentBoard);
        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    public void update() throws RemoteException {
        if (gameAcceptor.isMyTurn())
            infoLabel.setText("Your turn");
        else
            infoLabel.setText("Opponent's turn");
        PlayerBoard myBoard = gameAcceptor.getMyBoard();
        playerBoard.setPlayerBoard(myBoard);
        playerBoard.update();
        PlayerBoard hisBoard = gameAcceptor.getOpponentBoard();
        opponentBoard.setPlayerBoard(hisBoard);
        opponentBoard.update();
    }

    public void startGame(String gameId) {
        try {
            gameAcceptor = (GameAcceptor)registry.lookup(Config.GAME_PREFIX + playerId);
            opponentBoard.setGameAcceptor(gameAcceptor);
            update();
        } catch (RemoteException | NotBoundException e) {
            LOG.error("Can't get game acceptor", e);
        }
    }

    public void setWin() {
        infoLabel.setText("You win!!!");
    }

    public void setLose() {
        infoLabel.setText("You lose!!!");
    }
}
