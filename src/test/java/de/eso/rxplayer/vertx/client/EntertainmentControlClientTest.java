package de.eso.rxplayer.vertx.client;

import static org.junit.jupiter.api.Assertions.*;

import de.eso.rxplayer.Audio;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntertainmentControlClientTest {

  EntertainmentControlClient clientInstance;

  @BeforeEach
  void setUp() {
    clientInstance = Launcher.getClient().blockingGet();
    log("Created client");
    clientInstance.start();
    log("Started client");
  }

  @Test
  void requests() {
    clientInstance
        .newRequest("/browse/sources", Audio.Connection.class)
        .subscribe(
            apiResponse -> {
              System.out.println(apiResponse.getBody());
            });
  }

  @AfterEach
  void tearDown() {
    try {
      Thread.sleep(3_000);
    } catch (InterruptedException e) {

    }
  }

  private static void log(String s) {
    System.out.println("[" + LocalDateTime.now() + "] " + s);
  }
}
