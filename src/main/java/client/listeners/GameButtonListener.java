package client.listeners;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.interfaces.GameAcceptor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

@AllArgsConstructor
public class GameButtonListener implements ActionListener {

    private static Logger LOG = LoggerFactory.getLogger(GameButtonListener.class);

    private int pos_y;
    private int pos_x;
    private GameAcceptor gameAcceptor;

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            gameAcceptor.getTurn(pos_x, pos_y);
        } catch (RemoteException remoteException) {
            LOG.error("Can't get turn", remoteException);
        }
    }
}
