package de.eso.rxplayer.vertx.client;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Audio;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntertainmentControlClientTest {

  EntertainmentControlClient clientInstance;

  @BeforeEach
  void setUp() {
    clientInstance = Launcher.getClient().blockingGet();
    clientInstance.start();
  }

  @Test
  void requestSources() {
    TestObserver<List> testObserver =
    clientInstance.newRequest("/browse/sources", Audio.Connection.class)
        .map(apiResponse -> apiResponse.getBody())
        .test();

    waitFor(1_000);
    testObserver.assertValue(l -> l.contains("CD") && l.contains("USB"));
  }

  @Test
  void tryToPlay() {
    clientInstance.newRequest("/browse/albums", Album.class)
        .map(res -> res.getBody())
        .subscribe(System.out::println);
    waitFor(2000);
  }

  void waitFor(long n) {
    try {
      Thread.sleep(n);
    } catch (InterruptedException e) {

    }
  }

  private static void log(String s) {
    System.out.println("[" + LocalDateTime.now() + "] " + s);
  }
}
