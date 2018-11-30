package de.eso.rxplayer.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerDisplay extends JFrame {

  public static void main(String[] args) {
    new MediaPlayerDisplay();
  }

  private static MediaPlayerDisplay mediaPlayerDisplay;

  private final Font font = new Font("SansSerif", Font.PLAIN, 40);

  private static final String RESET_TITLE = "--";
  private static final String RESET_TIME = "--:--";
  private static final String RESET_ARTIST = "--";
  private static final String RESET_ALBUM = "--";

  private final JLabel trackTimeLabel;

  private JLabel trackCover = new JLabel(getDefaultCover());

  private final JPanel trackPanel = new JPanel(new GridLayout(1, 2));

  private final List<PlayerButton> playerButtons = new ArrayList<>();

  @SuppressWarnings("WeakerAccess")
  public MediaPlayerDisplay() {
    mediaPlayerDisplay = this;
    mediaPlayerDisplay.setVisible(true);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    trackTimeLabel = new JLabel(RESET_TIME, SwingConstants.CENTER);
    trackTimeLabel.setFont(font);
    add(trackTimeLabel, BorderLayout.NORTH);

    trackPanel.add(trackCover, BorderLayout.WEST);
    trackPanel.add(new createTrackInformationPanel(), BorderLayout.EAST);

    JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
    addPlayerButtons(buttonPanel); //PlayerControl Buttons on the Bottom of the Player

    add(trackPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    pack();

    //ToDo implement switchView Button
    //ToDo implement a way to switch between sources
  }

  private void addPlayerButtons(JPanel buttonPanel) {
    playerButtons.add(new BackButton());
    playerButtons.add(new StopButton());
    playerButtons.add(new PlayPauseButton());
    playerButtons.add(new ForwardButton());

    for (PlayerButton button : playerButtons) {
      buttonPanel.add(button);
    }
  }

  private class createTrackInformationPanel extends JPanel {
    createTrackInformationPanel() {
      super(new BorderLayout());

      final JLabel trackTitleLabel;
      final JLabel trackArtistLabel;
      final JLabel trackAlbumLabel;

      trackTitleLabel = new JLabel(RESET_TITLE, SwingConstants.CENTER);
      trackTitleLabel.setFont(font.deriveFont(12.0f));
      add(trackTitleLabel, BorderLayout.NORTH);

      trackArtistLabel = new JLabel(RESET_ARTIST, SwingConstants.CENTER);
      trackArtistLabel.setFont(font.deriveFont(12.0f));
      add(trackArtistLabel, BorderLayout.CENTER);

      trackAlbumLabel = new JLabel(RESET_ALBUM, SwingConstants.CENTER);
      trackAlbumLabel.setFont(font.deriveFont(12.0f));
      add(trackAlbumLabel, BorderLayout.SOUTH);
    }
  }

  /**
   * change the color theme of the compete UI
   *
   * @param theme ColorTheme to switch to
   */
  public void changeColorTheme(ColorTheme theme) {
    for (PlayerButton button : playerButtons) {
      button.setStyling(theme);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public void setTrackTime(String s) {
    trackTimeLabel.setText(s);
  }

  public void resetTrackTime() {
    setTrackTime(RESET_TIME);
  }

  public void setTrackTitle(String title) {
    ((JLabel) ((JPanel) trackPanel.getComponent(2)).getComponent(0)).setText(title);
  }

  public void resetTrackTitle() {
    setTrackTitle(RESET_TITLE);
  }

  @SuppressWarnings("WeakerAccess")
  public void setTrackArtist(String title) {
    ((JLabel) ((JPanel) trackPanel.getComponent(2)).getComponent(1)).setText(title);
  }

  public void resetTrackArtist() {
    setTrackArtist(RESET_ARTIST);
  }

  public void setTrackAlbum(String title) {
    ((JLabel) ((JPanel) trackPanel.getComponent(2)).getComponent(2)).setText(title);
  }

  public void resetTrackAlbum() {
    setTrackAlbum(RESET_ALBUM);
  }

  /**
   * sets the coverImage of the Player to a picture behind the given url If no file could be found
   * behind the path nothing happens
   *
   * @param path to the file of the image
   */
  public void setCoverImage(String path) {
    BufferedImage myPicture = null;
    try {
      URL url = new URL(path);
      myPicture = ImageIO.read(url);
    } catch (IOException e) {
      System.out.println("[WARNING] CoverImage not found!");
    }

    ImageIcon image;
    if (myPicture == null) {
      image = getDefaultCover();
    } else {
      image = new ImageIcon(myPicture);
    }

    this.trackCover = new JLabel(resizeImage(image));
  }

  private ImageIcon resizeImage(ImageIcon imageIcon) {
    Image image = imageIcon.getImage(); // transform it
    Image newImage = image.getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH); // scale it
    return new ImageIcon(newImage); // transform it back
  }

  @SuppressWarnings("WeakerAccess")
  public ImageIcon getDefaultCover() {
    BufferedImage myPicture = null;
    try {
      myPicture = ImageIO.read(new File("src/main/resources/defaultCover.png"));
    } catch (IOException e) {
      System.out.println("[WARNING] Default CoverImage not found!");
    }

    ImageIcon image;
    if (myPicture == null) {
      return null; // this really shouldn't happen
    } else {
      image = new ImageIcon(myPicture);
    }

    return image;
  }

  public static MediaPlayerDisplay getMediaPlayerDisplay() {
    return mediaPlayerDisplay;
  }
}
