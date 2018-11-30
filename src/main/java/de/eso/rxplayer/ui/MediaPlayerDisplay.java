package de.eso.rxplayer.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerDisplay extends JFrame {

    public static void main(String[] args) {
        MediaPlayerDisplay mediaPlayerDisplay = new MediaPlayerDisplay();
        mediaPlayerDisplay.setVisible(true);
    }

    private final Font font = new Font("SansSerif", Font.PLAIN, 40);

    private final static String RESET_TITLE = "--";
    private final static String RESET_TIME = "--:--";
    private static final String RESET_ARTIST = "--";
    private static final String RESET_ALBUM = "--";

    private final JLabel trackTimeLabel;

    private JLabel trackCover = new JLabel(getDefaultCover());

    private JPanel trackPanel = new JPanel(new GridLayout(1,2));

    private List<PlayerButton> playerButtons = new ArrayList<>();



    /**
     * Initialisiert das Media-Display.
     */
    public MediaPlayerDisplay() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        trackTimeLabel = new JLabel(RESET_TIME, SwingConstants.CENTER);
        trackTimeLabel.setFont(font);
        add(trackTimeLabel, BorderLayout.NORTH);

        trackPanel.add(trackCover);
        trackPanel.add(new createTrackInformationPanel()); //right hand side with the track information

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        addPlayerButtons(buttonPanel); //PlayerControl Buttons on the Bottom of the Player

        add(trackPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();


        //ToDo implement switchView Button
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
            final JLabel trackTitleLabel;
            final JLabel trackArtistLabel;
            final JLabel trackAlbumLabel;

            JPanel trackInformationPanel = new JPanel(new GridLayout(3, 1));

            trackTitleLabel = new JLabel(RESET_TITLE, SwingConstants.CENTER);
            trackTitleLabel.setFont(font.deriveFont(12.0f));
            trackInformationPanel.add(trackTitleLabel);

            trackArtistLabel = new JLabel(RESET_TITLE, SwingConstants.CENTER);
            trackArtistLabel.setFont(font.deriveFont(12.0f));
            trackInformationPanel.add(trackTitleLabel);

            trackAlbumLabel = new JLabel(RESET_TITLE, SwingConstants.CENTER);
            trackAlbumLabel.setFont(font.deriveFont(12.0f));
            trackInformationPanel.add(trackTitleLabel);
        }
    }

    /**
     * change the color theme of the compete UI
     * @param theme ColorTheme to switch to
     */
    public void changeColorTheme(ColorTheme theme) {
        for (PlayerButton button : playerButtons) {
            button.setStyling(theme);
        }
    }

    public void setTrackTime(String s) {
        trackTimeLabel.setText(s);
    }
    public void resetTrackTime() {
        setTrackTime(RESET_TIME);
    }

    public void setTrackTitle(String title) {
        ((JLabel)((JPanel)trackPanel.getComponent(2)).getComponent(0)).setText(title);
    }
    public void resetTrackTitle() {
       setTrackTitle(RESET_TITLE);
    }

    public void setTrackArtist(String title) {
        ((JLabel)((JPanel)trackPanel.getComponent(2)).getComponent(1)).setText(title);
    }
    public void resetTrackArtist() {
        setTrackArtist(RESET_ARTIST);
    }

    public void setTrackAlbum(String title) {
        ((JLabel)((JPanel)trackPanel.getComponent(2)).getComponent(2)).setText(title);
    }
    public void resetTrackAlbum() {
        setTrackAlbum(RESET_ALBUM);
    }


    /**
     * sets the coverImage of the Player to a picture behind the given url
     * If no file could be found behind the path nothing happens
     * @param path to the file of the image
     */
    public void setCoverImage(String path) {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {

        }

        ImageIcon image;
        if (myPicture == null) {
            image = getDefaultCover();
        } else {
            image = new ImageIcon(myPicture);
        }

        trackCover = new JLabel(image);
    }

    public ImageIcon getDefaultCover() {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("src/main/resources/defaultCover.png"));
        } catch (IOException e) {

        }

        ImageIcon image;
        if (myPicture == null) {
            return null; //this really shouldn't happen
        } else {
            image = new ImageIcon(myPicture);
        }

        return image;
    }
}