package de.eso.rxplayer.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MediaBrowserDisplay extends JFrame {

  private static MediaBrowserDisplay mediaBrowserDisplay;

  private final List<PlayerButton> buttons = new ArrayList<>();

  public static void main(String[] args) {
    new MediaBrowserDisplay();
  }

  @SuppressWarnings("WeakerAccess")
  public MediaBrowserDisplay() {
    mediaBrowserDisplay = this;
    mediaBrowserDisplay.setVisible(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JPanel mainGrid = new JPanel(new GridLayout(3, 3));

    buttons.add(new DisplayAlbumsButton());
    buttons.add(new SearchAlbumButton());
    buttons.add(new SearchTrackButton());
    buttons.add(new AlbumTracksButton());
    buttons.add(new SwitchColorButton());
    buttons.add(new SwitchLanguageButton());

    for (PlayerButton button : buttons) {
      mainGrid.add(button);
    }

    add(mainGrid);

    pack();
  }

  public void changeLanguage(Language language) {
    for (PlayerButton button : buttons) {
      button.changeLanguage(language);
    }
  }

  public void changeColorTheme(ColorTheme colorTheme) {
    for (PlayerButton button : buttons) {
      button.setStyling(colorTheme);
    }
  }

  public static MediaBrowserDisplay getMediaBrowserDisplay() {
    return mediaBrowserDisplay;
  }
}
