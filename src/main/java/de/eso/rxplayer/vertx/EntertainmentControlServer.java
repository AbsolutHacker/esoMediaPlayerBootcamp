package de.eso.rxplayer.vertx;

import de.eso.rxplayer.api.ApiRequest;
import de.eso.rxplayer.api.ApiResponse;
import de.eso.rxplayer.api.MediaBrowser;
import de.eso.rxplayer.api.MediaPlayer;
import io.reactivex.Observable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;

import java.time.LocalDateTime;
import java.util.*;

public class EntertainmentControlServer {

    private static final int DEFAULT_LISTEN_PORT = 8091;

    private final MediaPlayer mediaPlayer;
    private final MediaBrowser mediaBrowser;
    private final Vertx vertx;

    EntertainmentControlServer(MediaPlayer mediaPlayer, MediaBrowser mediaBrowser) {
        this.mediaPlayer = mediaPlayer;
        this.mediaBrowser = mediaBrowser;
        this.vertx = Vertx.vertx();
    }

    void start() {
        vertx.createHttpServer(
            new HttpServerOptions()
            .setLogActivity(true)
        )
            .requestHandler(this::httpRequestHandler)
            .websocketHandler(this::webSocketHandler)
            .listen(DEFAULT_LISTEN_PORT);
    }

    private void webSocketHandler(ServerWebSocket serverWebSocket) {
        vertx.deployVerticle(new Butler(serverWebSocket));
    }

    private void httpRequestHandler(HttpServerRequest request) {
        HttpServerResponse res = request.response();
        res.write("HTTP connection not supported").setStatusCode(500).end();
    }

    private class Butler extends AbstractVerticle {

        private final ServerWebSocket socket;
        private final Map<Integer,ApiRequest> pendingRequests = new HashMap<>();

        Butler(ServerWebSocket serverWebSocket) {
            this.socket = serverWebSocket;
        }

        @Override
        public void start() {
            // register API handlers
            socket.handler(this::parseRequest);
        }

        @Override
        public void stop() {
            // close the socket
            socket.close();
        }

        private void parseRequest(Buffer buffer) {
            try {
                // TODO check up on how dangerous this is
                // no sanitization whatsoever has been performed up to this point
                ApiRequest requestPojo = buffer.toJsonObject().mapTo(ApiRequest.class);
                if (requestPojo.isValid()) {
                    if (pendingRequests.containsKey(requestPojo.id)) {
                        // request id exists, do something
                    } else {
                        pendingRequests.put(requestPojo.id, requestPojo);
                        handleRequest(requestPojo);
                    }
                }
            } catch (DecodeException | IllegalArgumentException e) {
                // eating the exception
                System.out.println("Yum-yum-yum, tasty exception...");
                System.err.println(LocalDateTime.now() + " Failed to parse client request, ignoring");
                e.getCause().printStackTrace();
            }



            // TODO check integrity of the request object:
            // - does it have an id?
            // - does it map to a valid operation?
            // - do the contents of the request match the operation signature?

            // TODO call the operation with the given arguments

            //      if the operation returns an observable,
            // TODO subscribe the response handler to that observable
        }

        private void handleRequest(ApiRequest request) {
            // log for my personal amusement
            System.out.println(Json.encodePrettily(request));

            Observable
                .just(
                    new ApiResponse(
                        request,
                        ApiResponse.Type.SUCCESS,
                        new HashMap<>()
                    )
                )
                .subscribe(this::handleResponse)
            ;
        }

        private void handleResponse(ApiResponse response) {
            socket.writeFinalTextFrame(Json.encodePrettily(response));
        }

    }

}