package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.Audio.AudioState;
import de.eso.rxplayer.api.MediaPlayer;
import io.reactivex.Completable;
import io.reactivex.Observable;
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
        Completable completable = Completable.fromObservable(playInner());
        completable.subscribe(() -> {
            System.out.println("complete");
        },
        throwable -> {
            System.out.println("error");
        });
    }


    private Observable playInner() {
        System.out.println("press play");
        Observable<Object> objectObservable = Observable.empty();
        if (player == null) {
            if(radio == null){
                selectSource(DEFAULT_SOURCE);
            } else { return Observable.empty();  } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState){
                case STOPPED:
                    break;
                case STARTING:
                    break;
                case STARTED:
                    objectObservable = player.isPlaying().switchMap((verifyPlaing) -> {
                        if (!verifyPlaing) {
                            System.out.println("not playing");
                            return player.play().toObservable();
                        }
                        System.out.println("playing");
                        return Completable.complete().toObservable();
                    });
                    break;
                case STOPPING:
                    break;
            }
        }
        return objectObservable;
    }

    @Override
    public void pause() {
        System.out.println("press pause");
        if (player == null) {
            if(radio == null){
                selectSource(DEFAULT_SOURCE);
            } else { return;  } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState){
                case STOPPED:
                    break;
                case STARTING:
                    break;
                case STARTED:
                    player.pause().subscribe(() -> {
                        System.out.println("complete");// handle completion
                    }, throwable -> {
                        System.out.println("error");
                        //throwable.printStackTrace();// handle error
                    });
                    break;
                case STOPPING:
                    break;
            }
        }
    }

    @Override
    public void stop() {
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        es.getAudio().fadeOut(currentSource);

        if(player == null && radio == null){
            selectSource(DEFAULT_SOURCE);
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
        if(currentSource == null){
            switchSource(source);
        }else if(currentSource != source){
            System.out.println("select source");
            es.getAudio().fadeOut(currentSource).subscribe(() -> {
                es.getAudio().stop(currentSource);
            }, Throwable::printStackTrace);
        }
    }

    private void switchSource(Audio.Connection source){
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        es.getAudio().observe(source)
                .subscribe(event -> {
                    currentState = event;
                    System.out.println( "Event fired: " + event.name());
                    switch (event)
                    {
                        case STARTED:
                            System.out.println("fade in: " + source.name());
                            es.getAudio().fadeIn(currentSource);
                            break;
                        case STOPPED:
                            try{
                                switch (source) {
                                    case USB:
                                        radio = null;
                                        player = es.getUsb();
                                        es.getAudio().start(Audio.Connection.USB);
                                        break;
                                    case CD:
                                        radio = null;
                                        player = es.getCd();
                                        es.getAudio().start(Audio.Connection.CD);
                                        break;
                                    case RADIO:
                                        player = null;
                                        radio = es.getFm();
                                        radio.select(lastRadioStation);
                                        es.getAudio().start(Audio.Connection.RADIO);
                                }
                            } catch (NotImplementedError e){
                                selectSource(DEFAULT_SOURCE);
                            }
                            break;
                        default:
                            System.out.println( event.name() + " -> " + "Nothing happens");
                    }
                });
        currentSource = source;
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
            Thread.sleep(1000);
            mp.play();
            Thread.sleep(1000);
            mp.play();
            Thread.sleep(1000);
            mp.play();
            Thread.sleep(1000);
            mp.play();
            Thread.sleep(1000);
            mp.play();
            //mp.switchSource(Audio.Connection.USB);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
