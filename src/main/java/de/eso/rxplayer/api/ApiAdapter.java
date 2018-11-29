package de.eso.rxplayer.api;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Track;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.vertx.core.Vertx;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class ApiAdapter {
  private final MediaBrowser browser;
  private final MediaPlayer player;

  public ApiAdapter(MediaPlayer mediaPlayer, MediaBrowser mediaBrowser) {
    this.player = mediaPlayer;
    this.browser = mediaBrowser;
  }

  public Callable<Observable> translate(String invocationTarget) {

    System.out.println("ApiAdapter#translate: invocationTarget = \'" + invocationTarget + "\'");
    if (invocationTarget == null) throw new IllegalArgumentException("ApiAdapter#translate: invocationTarget must not be null!");

    switch (invocationTarget) {
      case "/browse/albums":
        return browser::getAlbums;
      case "/browse/stations":
        return browser::getStations;
    }

    throw new AssertionError("ApiAdapter#translate: no valid mapping found!");

  }

  public Observable invokeAsync(String invocationTarget) throws Exception {
    switch (invocationTarget) {
      case "/timer":
        BehaviorSubject<Long> subject = BehaviorSubject.create();
        Vertx.vertx().setPeriodic(100, subject::onNext);
        return subject.hide();
    }
    return translate(invocationTarget).call();
  }

}
