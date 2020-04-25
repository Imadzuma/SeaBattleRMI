package server.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.data.Game;
import shared.data.PlayerBoard;
import shared.interfaces.GameAcceptor;

import java.rmi.RemoteException;

@AllArgsConstructor
public class GameAcceptorImpl implements GameAcceptor {

    private static Logger LOG = LoggerFactory.getLogger(GameAcceptor.class);

    private Game game;
    private int player;

    @Override
    public Boolean isMyTurn() {
        LOG.info("{} player ask turn", player);
        return player == game.getCurrentPlayer();
    }

    @Override
    public PlayerBoard getMyBoard() throws RemoteException {
        LOG.info("{} player ask his board", player);
        return game.getPlayerBoard(player);
    }

    @Override
    public PlayerBoard getOpponentBoard() throws RemoteException {
        LOG.info("{} player ask opponent's board", player);
        return game.getOpponentBoard(player);
    }

    @Override
    public boolean getTurn(int x, int y) throws RemoteException {
        LOG.info("{} player want to ge turn: x = {}, y = {}", player, x, y);
        return game.getTurn(player, x, y);
    }
}
