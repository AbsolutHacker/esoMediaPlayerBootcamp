package de.eso.rxplayer.vertx.client;

import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;

public class Launcher {
  public static void main(String[] args) {
    Vertx.vertx().createHttpClient()
        .websocket(8091, "localhost", "/", Launcher::instantiateClient);
  }
  private static void instantiateClient(WebSocket socket) {
    new EntertainmentControlClient(socket).start();
  }
}
