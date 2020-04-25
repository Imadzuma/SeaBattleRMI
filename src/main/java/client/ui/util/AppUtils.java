package client.ui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AppUtils {

    public static JLabel createLabel(String labelName, JComponent parent, Dimension dimension) {
        JLabel label = new JLabel(labelName);
        label.setFont(new Font("Dialog", Font.PLAIN, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(dimension);
        parent.add(label);
        return label;
    }

    public static JButton createButton(String name, JComponent parent, Dimension dimension, ActionListener actionListener) {
        JButton button = new JButton(name);
        button.setPreferredSize(dimension);
        button.setFont(new Font("Dialog", Font.PLAIN, 20));
        button.addActionListener(actionListener);
        parent.add(button);
        return button;
    }

}
