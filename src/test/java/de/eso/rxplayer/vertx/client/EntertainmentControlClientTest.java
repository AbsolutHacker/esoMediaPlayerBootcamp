package de.eso.rxplayer.vertx.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
    clientInstance.newRequest("/browse/albums")
        .subscribe(System.out::println);
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