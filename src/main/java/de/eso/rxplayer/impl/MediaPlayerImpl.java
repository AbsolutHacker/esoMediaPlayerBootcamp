package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.api.MediaPlayer;

/**
 * MediaPlayer with all its buttons and functionality for the user to control the player
 */
public final class MediaPlayerImpl implements MediaPlayer {
    private static MediaPlayerImpl mediaPlayer;
    private Player player = null;
    private Radio radio = null;
    private int lastRadioStation = 0;

    private MediaPlayerImpl() {
    }

    public static MediaPlayerImpl getInstance() {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayerImpl();
        }
        return mediaPlayer;
    }

    @Override
    public void play() {
        if (player == null && radio != null) {
            return; //Play was pressed in radio mode -> do nothing
        }

        //ToDo default case when nothing was initialized

        if (player != null) {
            player.play();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    @Override
    public void selectSource(Audio.Connection source) {
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        switch (source) {
            case USB:
                player = es.getUsb();
                break;
            case CD:
                player = es.getCd();
                break;
            case RADIO:
                player = null;
                radio = es.getFm();
                radio.select(lastRadioStation); //automatically start the radio
                break;
        }
    }

    @Override
    public void selectItem(int itemId) {

    }

    @Override
    public long getPlaytimeOffset() {
        return 0;
    }

    @Override
    public Track getCurrentTrack() {
        return null;
    }
}
