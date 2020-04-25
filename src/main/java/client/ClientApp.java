package client;

import client.ui.frames.MenuFrame;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuFrame::new);
    }
}
