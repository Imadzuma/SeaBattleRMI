package client.listeners;

import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@AllArgsConstructor
public class ExitListener implements ActionListener {

    private JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
    }
}
