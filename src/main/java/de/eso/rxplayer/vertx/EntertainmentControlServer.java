package de.eso.rxplayer.vertx;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Entertainment;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;

public class EntertainmentControlServer {

    public static final int DEFAULT_LISTEN_PORT = 8091;

    private final Entertainment entertainmentSystem;
    private final Vertx vertx;

    public EntertainmentControlServer(Entertainment entertainmentSystem) {
        this.entertainmentSystem = entertainmentSystem;
        this.vertx = Vertx.vertx();
    }

    public void start() {
        vertx.createHttpServer(
            new HttpServerOptions()
            .setLogActivity(true)
        )
            .requestHandler(this::httpRequestHandler)
            .websocketHandler(this::webSocketHandler)
            .listen(DEFAULT_LISTEN_PORT);
    }

    private void webSocketHandler(ServerWebSocket serverWebSocket) {
        System.out.println("Accepted WebSocket connection to path " + serverWebSocket.path());
        if (serverWebSocket.path().startsWith("/listen")) {
            entertainmentSystem.getAudio().observe(Audio.Connection.CD).subscribe(connectionState ->
                serverWebSocket.writeFinalTextFrame(connectionState.toString()));
        }
    }

    private void httpRequestHandler(HttpServerRequest request) {
        HttpServerResponse res = request.response();
        res.write("HTTP connection not supported").setStatusCode(500).end();
    }

}
