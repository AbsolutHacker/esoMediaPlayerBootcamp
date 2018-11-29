package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.Audio.AudioState;
import de.eso.rxplayer.api.MediaPlayer;
import io.reactivex.Completable;
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
    private Audio.Connection currentSource;
    private AudioState currentState = AudioState.STOPPED;

    private MediaPlayerImpl() {

    }

    public static MediaPlayerImpl getInstance() {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayerImpl();
        }
        return mediaPlayer;
    }

    @Override
    public void play(){
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        playInner()
                .mergeWith(es.getAudio().fadeIn(currentSource))
                .subscribe(() -> {
            System.out.println("play complete");
        }, Throwable::printStackTrace);
    }

    private Completable playInner() {
        System.out.println("press play");
        Completable startsPlaying = Completable.complete();
        if (player == null) {
            if(radio == null){
                selectSource(DEFAULT_SOURCE);
            } else { return Completable.complete();  } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState){
                case STOPPED:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
                case STARTING:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
                case STARTED:
                    startsPlaying = player.isPlaying()
                            .take(1)
                            .flatMapCompletable((verifyPlayState) -> {
                                if (!verifyPlayState) {
                                    System.out.println("not playing -> starts playing");
                                    return player.play();
                                } else {
                                    System.out.println("is playing -> do nothing");
                                    return Completable.complete();
                                }
                    });
                    break;
                case STOPPING:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
            }
        }
        return startsPlaying;
    }

    @Override
    public void pause(){
        System.out.println("press pause");
        EntertainmentService es = myEntertainmentService.getEntertainmentService();

        es.getAudio().fadeOut(currentSource)
                .mergeWith(pauseInner())
                .subscribe(() -> {
                    System.out.println("pause complete");
                },
                throwable -> {
                    throwable.printStackTrace();
                });
    }

    private Completable pauseInner() {
        System.out.println("call pauseInner");
        Completable startsPlaying = Completable.complete();
        if (player == null) {
            if(radio == null){
                selectSource(DEFAULT_SOURCE);
            } else {
                System.out.println("pause -> Player == null AND Radio != null");
                return Completable.complete();
            } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState){
                case STOPPED:
                    System.out.println("pause -> STOPPED");
                    return Completable.error(() -> new Exception("Wrong state for execute pause"));
                case STARTING:
                    System.out.println("pause -> STARTING");
                    return Completable.error(() -> new Exception("Wrong state for execute pause"));
                case STARTED:
                    System.out.println("pause -> STARTED");
                    startsPlaying = player.isPlaying()
                            .takeLast(1)
                            .flatMapCompletable((verifyPlayState) -> {
                                if (verifyPlayState) {
                                    System.out.println("is playing -> starts pausing");
                                    return player.pause();
                                } else {
                                    System.out.println("not playing -> do nothing");
                                    return Completable.complete();
                                }
                            });
                    break;
                case STOPPING:
                    System.out.println("pause -> STOPPING");
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
            }
        }
        return startsPlaying;
    }

    @Override
    public void stop(){
        EntertainmentService es = myEntertainmentService.getEntertainmentService();

        es.getAudio().fadeOut(currentSource)
            .mergeWith(playInner())
            .subscribe(() -> {
                    System.out.println("complete");
                },
                throwable -> {
                    throwable.printStackTrace();
                });
    }
    private Completable stopInner() {
        System.out.println("press play");
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        Completable startsPlaying = Completable.complete();
        if (player == null) {
            if(radio == null){
                selectSource(DEFAULT_SOURCE);
            } else { return Completable.complete();  } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState){
                case STOPPED:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
                case STARTING:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
                case STARTED:
                    es.getAudio().stop(currentSource);
                    break;
                case STOPPING:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
            }
        }
        return startsPlaying;
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
        if(currentSource == null){
            manageSources(source);
        }else if(currentSource != source){
            System.out.println("select source");
            es.getAudio().fadeOut(currentSource).subscribe(() -> {
                es.getAudio().stop(currentSource);
            });
        }
    }

    private void manageSources(Audio.Connection source){
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        es.getAudio().observe(source)
                .subscribe(event -> {
                    currentState = event;
                    System.out.println( "Event fired: " + event.name());
                    switch (event)
                    {
                        case STARTED:
                            System.out.println("fade in: " + source.name());
                            break;
                        case STOPPED:
                            stopped(source);
                            break;
                        default:
                            System.out.println( event.name() + " -> " + "Nothing happens");
                    }
                });
    }

    private void stopped(Audio.Connection source){
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        try{
            if(currentSource != source){
                switch (source) {
                    case USB:
                        radio = null;
                        player = es.getUsb();
                        break;
                    case CD:
                        radio = null;
                        player = es.getCd();
                        break;
                    case RADIO:
                        player = null;
                        radio = es.getFm();
                        radio.select(lastRadioStation);
                }
                currentSource = source;
                es.getAudio().start(source);
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
        try {
            MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
            mp.selectSource(Audio.Connection.CD);
            mp.play();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.pause();
            Thread.sleep(500);
            mp.pause();
            Thread.sleep(500);
            mp.pause();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.play();
            Thread.sleep(500);
            mp.pause();
            //mp.manageSources(Audio.Connection.USB);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
