package de.eso.rxplayer.ui;

import java.awt.event.ActionListener;
import javax.swing.*;

abstract class PlayerButton extends JButton {
  PlayerButton() {
    this.setText(getText());
    this.addActionListener(getActionListeners()[0]);
  }

  @Override
  public abstract String getText();

  @Override
  public abstract ActionListener[] getActionListeners();
}
