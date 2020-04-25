package server.data;

import com.google.gson.Gson;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.services.GameAcceptorImpl;
import server.threads.GameManager;
import server.utils.FileUtils;
import shared.config.Config;
import shared.data.FieldType;
import shared.data.PlayerBoard;
import shared.interfaces.GameAcceptor;
import shared.interfaces.MessageAcceptor;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Getter
public class Game {

    private static Logger LOG = LoggerFactory.getLogger(Game.class);

    private Registry registry;
    private String gameId;
    private String[] playerId;
    private MessageAcceptor[] messageAcceptor;
    private GameAcceptor[] gameAcceptor;
    private int currentPlayer = 0;
    private PlayerBoard[] playerBoards;
    private Ship ships[][] = new Ship[2][];
    private boolean gameFinished = false;
    private GameManager gameManager;

    public Game(Registry registry, String  id, String firstPlayerId, String secondPlayerId, MessageAcceptor firstAcceptor, MessageAcceptor secondAcceptor, GameManager gameManager) {
        this.gameManager = gameManager;
        this.registry = registry;
        messageAcceptor = new MessageAcceptor[]{firstAcceptor, secondAcceptor};
        gameAcceptor = new GameAcceptor[2];
        playerId = new String[]{firstPlayerId, secondPlayerId};
        try {
            for (int i = 0; i < 2; ++i) {
                gameAcceptor[i] = new GameAcceptorImpl(this, i);
                GameAcceptor stub = (GameAcceptor) UnicastRemoteObject.exportObject(gameAcceptor[i], 0);
                registry.rebind(Config.GAME_PREFIX + playerId[i], stub);
                LOG.info("Regist game acceptor: {}", Config.GAME_PREFIX + playerId[i]);
            }
        } catch (RemoteException e) {
            LOG.error("Can't create game acceptors", e);
        }
        gameId = id;
        playerBoards = new PlayerBoard[] {new PlayerBoard(), new PlayerBoard()};
        Gson g = new Gson();
        try {
            String firstShips = FileUtils.getStringFromFile("src\\main\\resources\\templates\\first.json");
            ships[0] = g.fromJson(firstShips, Ship[].class);
        } catch (IOException e) {
            LOG.error("Can't load ships for first player", e);
        }
        try {
            String secondShips = FileUtils.getStringFromFile("src\\main\\resources\\templates\\second.json");
            ships[1] = g.fromJson(secondShips, Ship[].class);
        } catch (IOException e) {
            LOG.error("Can't load ships for second player", e);
        }
        updateFields();
        try {
            messageAcceptor[0].setGameId(gameId);
            messageAcceptor[1].setGameId(gameId);
            LOG.info("Start game {}", gameId);
        } catch (RemoteException e) {
            LOG.warn("Can't create connection with clients: ", e);
        }
    }

    public PlayerBoard getPlayerBoard(int player) {
        return playerBoards[player];
    }

    public PlayerBoard getOpponentBoard(int player) {
        PlayerBoard opponentBoard = new PlayerBoard();
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                opponentBoard.fields[i][j] = (playerBoards[1-player].fields[i][j] != FieldType.BUSY) ?
                        playerBoards[1-player].fields[i][j] : FieldType.EMPTY;
            }
        }
        return opponentBoard;
    }

    private void updateFields() {
        for (int player = 0; player < 2; ++player) {
            for (int i = 0; i < ships[player].length; ++i) {
                if (ships[player][i].alive) {
                    for (int j = 0; j < ships[player][i].type; ++j) {
                        int x = ships[player][i].x_coords[j];
                        int y = ships[player][i].y_coords[j];
                        playerBoards[player].fields[y][x] = (ships[player][i].alive_coords[j]) ? FieldType.BUSY : FieldType.INJURED;
                    }
                } else {
                    for (int j = 0; j < ships[player][i].type; ++j) {
                        int x = ships[player][i].x_coords[j];
                        int y = ships[player][i].y_coords[j];
                        playerBoards[player].fields[y][x] = FieldType.KILLED;
                    }
                }
            }
        }
    }

    public boolean getTurn(int player, int x, int y) {
        if (player != currentPlayer)
            return false;
        switch (playerBoards[1-player].fields[y][x]) {
            case TOUCHED:
                LOG.info("Field[{}][{}] has already touched", y, x);
                return false;
            case INJURED:
                LOG.info("Field[{}][{}] has already injured", y, x);
                return false;
            case KILLED:
                LOG.info("Field[{}][{}] has already killed", y, x);
                return false;
            case EMPTY:
                LOG.info("Field[{}][{}] is empty", y, x);
                playerBoards[1-player].fields[y][x] = FieldType.TOUCHED;
                currentPlayer = 1 - currentPlayer;
                try {
                    messageAcceptor[0].needUpdate();
                    messageAcceptor[1].needUpdate();
                } catch (RemoteException e) {
                    LOG.error("can't connect to messageAcceptor", e);
                }
                return true;
            case BUSY:
                LOG.info("Field[{}][{}] is busy", y, x);
                for (int i = 0; i < ships[1-player].length; ++i)
                    ships[1-player][i].shot(x, y);
                updateFields();
                try {
                    messageAcceptor[0].needUpdate();
                    messageAcceptor[1].needUpdate();
                } catch (RemoteException e) {
                    LOG.error("can't connect to messageAcceptor", e);
                }
        }
        for (int i = 0; i < ships[1-player].length; ++i)
            if (ships[1-player][i].alive) return true;
        try {
            messageAcceptor[player].yourAreWin();
            messageAcceptor[1-player].yourAreLose();
        } catch (RemoteException e) {
            LOG.error("can't connect to messageAcceptor", e);
        }
        gameManager.closeGame();
        return true;
    }
}
