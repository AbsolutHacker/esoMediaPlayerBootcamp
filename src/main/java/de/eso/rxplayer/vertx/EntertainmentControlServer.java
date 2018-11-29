package de.eso.rxplayer.vertx;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.api.*;
import de.eso.rxplayer.api.impl.Responder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class EntertainmentControlServer {

    private static final int DEFAULT_LISTEN_PORT = 8091;

    private final ApiAdapter apiAdapter;
    private final Vertx vertx;

    EntertainmentControlServer(MediaPlayer mediaPlayer, MediaBrowser mediaBrowser) {
        this.apiAdapter = new ApiAdapter(mediaPlayer, mediaBrowser);
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
                System.err.println(LocalDateTime.now() + " Failed to parse client request, ignoring");
                e.getCause().printStackTrace();
            }

        }

        @SuppressWarnings("unchecked")
        private void handleRequest(ApiRequest request) {

            // log for my personal amusement
            System.out.println(Json.encodePrettily(request));

            // TODO check integrity of the request object:
            // - do the contents of the request match the operation signature?

            // TODO call the operation with the given arguments

            //      if the operation returns an observable,
            // TODO subscribe the response handler to that observable

            try {
                apiAdapter.invokeAsync(request.params.get("target").toString())
                    .subscribe(new Responder(request, this::writeResponse));
            } catch (Exception e) {
                System.out.println("Something went wrong while trying to invoke an ApiAdapter function");
                e.printStackTrace();
            }

        }

        private void writeResponse(ApiResponse response) {
            socket.writeFinalTextFrame(Json.encodePrettily(response));
        }

    }

}