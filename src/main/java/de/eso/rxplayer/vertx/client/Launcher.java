package de.eso.rxplayer.vertx.client;

import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;

public class Launcher {

  public static Single<EntertainmentControlClient> getClient() {
    return Single.create(
        emitter ->
            Vertx.vertx()
                .createHttpClient()
                .websocket(
                    8091,
                    "localhost",
                    "/",
                    webSocket -> emitter.onSuccess(new EntertainmentControlClient(webSocket))));
  }

  public static void main(String[] args) {
    EntertainmentControlClient clientInstance = getClient().blockingGet();
    clientInstance.start();
    // just a playful test request
    clientInstance
        .newRequest("/browse/sources")
        .subscribe(
            response -> {
              System.err.println(Json.encodePrettily(response));
            });
  }
}
