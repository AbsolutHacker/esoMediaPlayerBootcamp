package de.eso.rxplayer.api;

import de.eso.rxplayer.Audio;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.vertx.core.Vertx;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public final class ApiAdapter {
  private final MediaBrowser browser;
  private final MediaPlayer player;

  public ApiAdapter(MediaPlayer mediaPlayer, MediaBrowser mediaBrowser) {
    this.player = mediaPlayer;
    this.browser = mediaBrowser;
  }

  public Callable<Observable> translate(String invocationTarget) {

    System.out.println("ApiAdapter#translate: invocationTarget = \'" + invocationTarget + "\'");

    if (invocationTarget == null) {
      throw new IllegalArgumentException(
          "ApiAdapter#translate: invocationTarget must not be null!");
    }

    switch (invocationTarget) {
      case "/browse/albums":
        return browser::getAlbums;
      case "/browse/stations":
        return browser::getStations;
    }

    throw new AssertionError("ApiAdapter#translate: no valid mapping found!");
  }

  public Observable invokeAsync(String invocationTarget) throws Exception {

    // intercept special cases, such as invocation targets not returning observables
    switch (invocationTarget) {
      // these are the player's (void) methods
      case "/play/play":
        player.play();
        return Observable.empty();
      case "/play/pause":
        player.pause();
        return Observable.empty();
      case "/play/stop":
        player.stop();
        return Observable.empty();
      case "/play/next":
        player.next();
        return Observable.empty();
      case "/play/back":
        player.previous();
        return Observable.empty();
      case "/play/select/cd":
        player.selectSource(Audio.Connection.CD);
        return Observable.empty();
      case "/play/select/usb":
        player.selectSource(Audio.Connection.USB);
        return Observable.empty();
      case "/play/select/radio":
        player.selectSource(Audio.Connection.RADIO);
        return Observable.empty();

      case "/browse/mock":
        return browser.searchAlbum("Fetty Wap");
      case "/browse/sources":
        BehaviorSubject<List<Audio.Connection>> mockSubject = BehaviorSubject.create();
        mockSubject.onNext(browser.getAvailableSources());
        Vertx.vertx().setTimer(3_000, (_unused) -> mockSubject.onComplete());
        return mockSubject.hide();
      case "/timer":
        BehaviorSubject<Long> subject = BehaviorSubject.create();
        Vertx.vertx().setPeriodic(1_000, subject::onNext);
        return subject.hide();
      default:

        Stream<String> invocationPath = Arrays.stream(invocationTarget.split("[^$/]"))
            .filter(string -> !string.isEmpty());

        if (invocationPath.count() < 2) {
          return Observable.empty();
        } else {
          // build context

        }

        return translate(invocationTarget).call();
    }
  }
}
