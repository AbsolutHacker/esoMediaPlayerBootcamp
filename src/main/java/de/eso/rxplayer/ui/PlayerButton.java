package de.eso.rxplayer.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

abstract class PlayerButton extends JButton {
    final static ColorTheme DEFAULT_COLOR_THEME = ColorTheme.ESO;

    PlayerButton() {
        this(DEFAULT_COLOR_THEME);
    }

    PlayerButton(ColorTheme colorTheme) {
        this.setText(getText());
        this.addActionListener(getActionListeners()[0]);
        this.setStyling(colorTheme);
    }

    public void setStyling(ColorTheme colorTheme) {
        this.setBackground(new Color(colorTheme.getHexCode()));
    }

    @Override
    abstract public String getText();

    @Override
    abstract public ActionListener[] getActionListeners();
}
