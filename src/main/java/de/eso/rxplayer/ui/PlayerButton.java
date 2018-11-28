package de.eso.rxplayer.ui;

import javax.swing.*;
import java.awt.event.ActionListener;

abstract class PlayerButton extends JButton {
    PlayerButton() {
        this.setText(getText());
        this.addActionListener(getActionListeners()[0]);
    }

    @Override
    abstract public String getText();

    @Override
    abstract public ActionListener[] getActionListeners();
}
