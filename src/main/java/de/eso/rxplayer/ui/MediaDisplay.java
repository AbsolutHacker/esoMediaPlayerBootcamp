package de.eso.rxplayer.ui;

import javax.swing.*;
import java.awt.*;

public class MediaDisplay extends JFrame {

    public static void main(String[] args) {
        MediaDisplay mediaDisplay = new MediaDisplay();
        mediaDisplay.setVisible(true);
    }


    private final static String RESET_TITLE = "--";
    private final static String RESET_TIME = "--:--";

    private final JLabel trackTimeLabel;
    private final JLabel trackTitleLabel;

    /**
     * Initialisiert das Media-Display.
     */
    public MediaDisplay() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        trackTimeLabel = new JLabel(RESET_TIME, SwingConstants.CENTER);
        Font font = new Font("SansSerif", Font.PLAIN, 40);
        trackTimeLabel.setFont(font);
        add(trackTimeLabel, BorderLayout.CENTER);

        trackTitleLabel = new JLabel(RESET_TITLE, SwingConstants.CENTER);
        trackTitleLabel.setFont(font.deriveFont(12.0f));
        add(trackTitleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        //back, stop, play, pause, forward
        buttonPanel.add(new BackButton());
        buttonPanel.add(new StopButton());
        buttonPanel.add(new PlayButton());
        buttonPanel.add(new PauseButton());
        buttonPanel.add(new ForwardButton());

        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    /**
     * Setzt den Text auf dem Media-Display.
     */
    public void setTrackTime(String s) {
        trackTimeLabel.setText(s);
    }

    /**
     * Setzt den Text auf dem Media-Display zurück.
     */
    public void resetTrackTime() {
        trackTimeLabel.setText(RESET_TIME);
    }

    /**
     * Setzt den Titel auf dem Media-Display.
     */
    public void setTrackTitle(String title) {
        trackTitleLabel.setText(title);
    }

    /**
     * Setzt den Titel auf dem Media-Display zurück.
     */
    public void resetTrackTitle() {
        trackTitleLabel.setText(RESET_TITLE);
    }

}