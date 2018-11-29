package de.eso.rxplayer.api;

import de.eso.rxplayer.Audio;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.vertx.core.Vertx;
import java.util.List;
import java.util.concurrent.Callable;

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

    String[] targetPath = invocationTarget.split("[/$^]");

    switch (targetPath[1]) {
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
      case "/browse/sources":
        BehaviorSubject<List<Audio.Connection>> mockSubject = BehaviorSubject.create();
        mockSubject.onNext(browser.getAvailableSources());
        Vertx.vertx().setTimer(3_000, (_unused) -> mockSubject.onComplete());
        return mockSubject.hide();
      case "/timer":
        BehaviorSubject<Long> subject = BehaviorSubject.create();
        Vertx.vertx().setPeriodic(100, subject::onNext);
        return subject.hide();
      default:
        return translate(invocationTarget).call();
    }
  }
}
