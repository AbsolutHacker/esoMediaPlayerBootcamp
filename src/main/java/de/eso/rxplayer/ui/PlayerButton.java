package de.eso.rxplayer.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

abstract class PlayerButton extends JButton {

  PlayerButton() {
    this.changeLanguage(Language.getDefaultLanguage());
    this.setText(getText());
    this.addActionListener(getActionListeners()[0]);
    this.setStyling(ColorTheme.getDefaultColorTheme());
  }

  abstract void changeLanguage(Language language);

  public void setStyling(ColorTheme colorTheme) {
    this.setBackground(new Color(colorTheme.getHexCode()));
  }

  @Override
  public abstract String getText();

  @Override
  public abstract ActionListener[] getActionListeners();
}
