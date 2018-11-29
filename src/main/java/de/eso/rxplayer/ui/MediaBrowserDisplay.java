package de.eso.rxplayer.ui;

import javax.swing.*;
import java.awt.*;

public class MediaBrowserDisplay extends JFrame {
    public static void main(String[] args) {
        MediaPlayerDisplay mediaPlayerDisplay = new MediaPlayerDisplay();
        mediaPlayerDisplay.setVisible(true);
    }

    public MediaBrowserDisplay() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Font font = new Font("SansSerif", Font.PLAIN, 40);
        pack();
    }
}
