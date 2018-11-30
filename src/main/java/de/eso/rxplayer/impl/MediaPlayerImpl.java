package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.Audio.AudioState;
import de.eso.rxplayer.api.MediaPlayer;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import kotlin.NotImplementedError;

import java.sql.Timestamp;

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
    private Subject<ServerEvents> playSubject;
    private Integer currentTrackId = 0;


    private MediaPlayerImpl() {
        playSubject = ReplaySubject.create();
        scheduleIncommingEvents();
    }

    public static MediaPlayerImpl getInstance() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayerImpl();
        }
        return mediaPlayer;
    }

    /**
     * starts the playback
     */
    @Override
    public void play() {
        System.out.println("press play");
        playSubject.onNext(ServerEvents.PLAY);
    }

    /**
     * pause the playback
     */
    @Override
    public void pause() {
        System.out.println("press pause");
        playSubject.onNext(ServerEvents.PAUSE);
    }

    /**
     * stops the player
     */
    @Override
    public void stop() {
        System.out.println("press stop");
        playSubject.onNext(ServerEvents.STOP);

    }

    /**
     * takes all events that will be invoke from outside
     * filter repeated events
     */
    private void scheduleIncommingEvents(){
        playSubject
            .subscribe(

            event -> {
                System.out.println("Next Event -> " + event.name());
                switch (event)
                {
                    case PLAY:
                        playInner(currentTrackId)
                            .subscribe(() -> {
                                    System.out.println("play complete");
                                    System.out.println(" ");
                                },
                                throwable -> {
                                    System.out.println("play failed");
                                    throwable.printStackTrace();
                                });
                        break;
                    case PAUSE:
                        pauseInner()
                            .subscribe(() -> {
                                    System.out.println("pause complete");
                                    System.out.println(" ");
                                },
                                throwable -> {
                                    System.out.println("pause failed");
                                    throwable.printStackTrace();
                                });
                        break;
                    case STOP:
                        stopInner()
                            .subscribe(() -> {
                                    System.out.println("stop complete");
                                    System.out.println(" ");
                                },
                                throwable -> {
                                    System.out.println("stop failed");
                                    throwable.printStackTrace();
                                });
                        break;

                    case CHANGESRC:
                        break;
                }
            }
        );
    }

    /**
     * manage the play state in different audio cases
     * @param track id of the track they should play
     *
     * @return
     */
    private Completable playInner(Integer track) {
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        Completable startsPlaying = Completable.complete();
        if (player == null) {
            if (radio == null) {
                selectSource(DEFAULT_SOURCE);
            } else {
                return Completable.complete();
            } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState) {
                case STOPPED:
                    return Completable.fromAction(() -> es.getAudio().start(currentSource));
                case STARTING:
                    return Completable.error(() -> new Exception("Wrong state for execute play"));
                case STARTED:
                    startsPlaying = player.isPlaying()
                        .take(1)
                        .flatMapCompletable((verifyPlayState) -> {
                            if (!verifyPlayState) {
                                System.out.println("not playing -> starts playing track " + track);
                                return player.select(track)
                                        .doOnComplete(() -> player.play())
                                        .doOnComplete(() -> es.getAudio().fadeIn(currentSource));
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

    /**
     * manage pause state in different audio cases
     *
     * @return
     */
    private Completable pauseInner() {
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        System.out.println("call pauseInner");
        Completable startsPlaying = Completable.complete();
        if (player == null) {
            if (radio == null) {
                selectSource(DEFAULT_SOURCE);
            } else {
                System.out.println("pause -> Player == null AND Radio != null");
                return Completable.complete();
            } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState) {
                case STOPPED:
                    System.out.println("pause -> STOPPED");
                    return Completable.error(() -> new Exception("Wrong state for execute pause"));
                case STARTING:
                    System.out.println("pause -> STARTING");
                    return Completable.error(() -> new Exception("Wrong state for execute pause"));
                case STARTED:
                    System.out.println("pause -> STARTED");
                    startsPlaying = player.isPlaying()
                            .take(1)
                            .flatMapCompletable((verifyPlayState) -> {
                                if (verifyPlayState) {
                                    System.out.println("is playing -> starts pausing");
                                    return es.getAudio().fadeOut(currentSource)
                                    .doOnComplete(() -> player.pause());
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

    /**
     * manage stop case in different audio cases
     *
     * @return
     */
    private Completable stopInner() {
        System.out.println("call stopInner");
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        Completable startsPlaying = Completable.complete();
        if (player == null) {
            if (radio == null) {
                selectSource(DEFAULT_SOURCE);
            } else {
                return Completable.complete();
            } //Play was pressed in radio mode -> do nothing
        } else {
            switch (currentState) {
                case STOPPED:
                    System.out.println("stop -> STOPPED");
                    return Completable.complete();
                case STARTING:
                    System.out.println("stop -> STARTING");
                    return Completable.complete();
                case STARTED:
                    System.out.println("stop -> STARTED");
                    return pauseInner()
                            .doOnComplete(() -> es.getAudio().stop(currentSource));
                case STOPPING:
                    System.out.println("stop -> STOPPING");
                    return Completable.complete();
            }
        }
        return startsPlaying;
    }

    /**
     * switch to the next track and starts playing automatically
     */
    @Override
    public void next() {
        System.out.println("press next");
        pauseInner()
            .doOnComplete(
                () -> player.nowPlaying()
                    .take(1)
                    .doOnNext((track) -> System.out.println("next -> current track: " + track))
                    .flatMapCompletable(trackid -> playInner(trackid + 1))
            )
            .subscribe(
                () -> {
                    System.out.println("next complete");
                    System.out.println(" ");
                },
                throwable -> {
                    System.out.println("stop failed");
                    throwable.printStackTrace();
                });
    }
    /**
     * switch to the previous track and starts playing automatically
     */
    @Override
    public void previous() {
        System.out.println("press previous");
        pauseInner()
                .doOnComplete(
                        () -> player.nowPlaying()
                                .take(1)
                                .flatMapCompletable(trackid -> {
                                    if(trackid > 0){
                                        return playInner(trackid + 1);
                                    } else {
                                        return Completable.complete();
                                    }

                                })
                )
                .subscribe(
                        () -> {
                            System.out.println("previous complete");
                            System.out.println(" ");
                        },
                        throwable -> {
                            System.out.println("previous failed");
                            throwable.printStackTrace();
                        });
    }

    /**
     * select a new source and stop the current started source
     *
     * @param source audio source
     */
    @Override
    public void selectSource(Audio.Connection source) {
        if (currentSource == null) {
            System.out.println("select new source");
            manageSources(source);
        } else if (currentSource != source) {
            System.out.println("switch source");
            stopInner()
                .doOnComplete(() -> manageSources(source))
                .doOnError( Throwable::printStackTrace)
                .subscribe(() -> {
                        System.out.println("switch source complete");
                        System.out.println(" ");
                    },
                    throwable -> {
                        System.out.println("switch source failed");
                        System.out.println(" ");
                        throwable.printStackTrace();
                    });

        }
    }

    /**
     * state event loop
     *
     * @param source
     */
    private void manageSources(Audio.Connection source) {
        System.out.println("call manageSources");
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        es.getAudio().observe(source)
            .subscribe(state -> {
                currentState = state;
                System.out.println("Event fired -> " + state.name());
                switch (state) {
                    case STARTED:
                        System.out.println("manageSources:  STARTED -> " + source.name() + " -> " + "Nothing happens");
                        break;
                    case STOPPED:
                        System.out.println("manageSources: STOPPED -> " + currentSource + " != " + source);
                        if (currentSource != source) {
                            changeSource(source);
                        }
                        break;
                    case STARTING:
                        System.out.println("manageSources:  STARTING -> " + state.name() + " -> " + "Nothing happens");
                        break;
                    default:
                        System.out.println("manageSources:  DEFAULT -> " + state.name() + " -> " + "Nothing happens");
                }
            });
    }

    /**
     * inner methode to switch between sources
     *
     * @param source audio source
     */
    private void changeSource(Audio.Connection source) {
        System.out.println("call changeSource");
        EntertainmentService es = myEntertainmentService.getEntertainmentService();
        try {
            if (currentSource != source) {
                switch (source) {
                    case USB:
                        System.out.println("changed source to -> " + source.name());
                        radio = null;
                        player = es.getUsb();
                        break;
                    case CD:
                        System.out.println("changed source to -> " + source.name());
                        radio = null;
                        player = es.getCd();
                        break;
                    case RADIO:
                        System.out.println("changed source to -> " + source.name());
                        player = null;
                        radio = es.getFm();
                        radio.select(lastRadioStation);
                }
                currentSource = source;
            }

        } catch (NotImplementedError e) {
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
            Integer ms = 2100;
            MediaPlayerImpl mp = MediaPlayerImpl.getInstance();
            mp.selectSource(Audio.Connection.CD);
            Thread.sleep(4000);
            mp.play();
            Thread.sleep(ms);
            mp.pause();
            Thread.sleep(ms);
            mp.play();
            Thread.sleep(ms);
            mp.next();
            Thread.sleep(ms);
            mp.play();
            Thread.sleep(ms);
            mp.pause();
            Thread.sleep(ms);
            mp.stop();
            Thread.sleep(ms);
            mp.selectSource(Audio.Connection.USB);
            Thread.sleep(ms);
//            mp.selectSource(Audio.Connection.USB);
//            Thread.sleep(ms);
//            mp.selectSource(Audio.Connection.USB);
//            Thread.sleep(ms);
//            mp.selectSource(Audio.Connection.USB);
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private enum ServerEvents{
        PLAY, PAUSE, STOP, CHANGESRC, ;
    }
}
