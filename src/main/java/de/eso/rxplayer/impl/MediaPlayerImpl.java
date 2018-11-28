package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.api.MediaPlayer;
import kotlin.NotImplementedError;

/**
 * MediaPlayer with all its buttons and functionality for the user to control the player
 */
public final class MediaPlayerImpl implements MediaPlayer {
    private static MediaPlayerImpl mediaPlayer;
    private static final Audio.Connection DEFAULT_SOURCE = Audio.Connection.CD;
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
        if(player == null && radio == null){
            selectSource(Audio.Connection.RADIO);
        }
        if (player == null && radio != null) {
            return; //Play was pressed in radio mode -> do nothing
        }

        if (player != null) {
            player.play().subscribe(() -> {
                System.out.println("complete");// handle completion
            }, throwable -> {

                System.out.println("error");
                throwable.printStackTrace();// handle error
            });
        }
    }

    @Override
    public void pause() {
        if(player == null && radio == null){
            selectSource(Audio.Connection.RADIO);
        }
        if (player == null && radio != null) {
            return; //Play was pressed in radio mode -> do nothing
        }

        if (player != null) {
            player.pause().subscribe(() -> {
                return;
            }, throwable -> {
                throw new Exception("test");
            });
        }

    }

    @Override
    public void stop() {
        if(player == null && radio == null){
            selectSource(Audio.Connection.RADIO);
        }
        if (player == null && radio != null) {
            return; //Play was pressed in radio mode -> do nothing
        }

        if (player != null) {
            player.pause().subscribe(() -> {
                return;
            }, throwable -> {
                throw new Exception("test");
            });
        }

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
        try{
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
                radio.select(lastRadioStation);
        }
        } catch (NotImplementedError e){
           selectSource(DEFAULT_SOURCE);
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

    public static void main(String[] args) {
        MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
        mp.play();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
