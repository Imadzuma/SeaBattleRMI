package client.ui.panels;

import client.listeners.GameButtonListener;
import client.ui.util.AppUtils;
import lombok.Setter;
import shared.data.PlayerBoard;
import shared.interfaces.GameAcceptor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Board extends JPanel {

    @Setter
    private PlayerBoard playerBoard = new PlayerBoard();
    private ImageIcon emptyImage, touchedImage, busyImage, injuredImage, killedImage;
    private JButton[][] gameButtons = new JButton[10][10];

    public Board() throws IOException {
        emptyImage = new ImageIcon(ImageIO.read(new File("src\\main\\resources\\images\\empty.jpg")));
        touchedImage = new ImageIcon(ImageIO.read(new File("src\\main\\resources\\images\\touched.jpg")));
        busyImage = new ImageIcon(ImageIO.read(new File("src\\main\\resources\\images\\busy.jpg")));
        injuredImage = new ImageIcon(ImageIO.read(new File("src\\main\\resources\\images\\injured.jpg")));
        killedImage = new ImageIcon(ImageIO.read(new File("src\\main\\resources\\images\\killed.jpg")));
        setLayout(new GridLayout(11, 11));
        AppUtils.createLabel("", this, new Dimension(25, 25));
        for (char i = 'A'; i < 'K'; ++i)
            AppUtils.createLabel(String.valueOf(i), this, new Dimension(25, 25));
        for (int i = 0; i < 10; ++i) {
            AppUtils.createLabel(String.valueOf(i+1), this, new Dimension(25, 25));
            for (int j = 0; j < 10; ++j) {
                gameButtons[i][j] = new JButton(emptyImage);
                gameButtons[i][j].setPreferredSize(new Dimension(25, 25));
                gameButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                add(gameButtons[i][j]);
            }
        }
    }

    public void setGameAcceptor(GameAcceptor gameAcceptor) {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j)
                gameButtons[i][j].addActionListener(new GameButtonListener(i, j, gameAcceptor));
        }
    }

    public void update() {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                switch (playerBoard.fields[i][j]) {
                    case EMPTY: gameButtons[i][j].setIcon(emptyImage); break;
                    case BUSY: gameButtons[i][j].setIcon(busyImage); break;
                    case TOUCHED: gameButtons[i][j].setIcon(touchedImage); break;
                    case INJURED: gameButtons[i][j].setIcon(injuredImage); break;
                    case KILLED:gameButtons[i][j].setIcon(killedImage); break;
                }
            }
        }
    }

}
