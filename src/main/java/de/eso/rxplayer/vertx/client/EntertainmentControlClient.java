package de.eso.rxplayer.vertx.client;

import de.eso.rxplayer.api.ApiResponse;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;

import java.util.HashMap;
import java.util.Map;

public class EntertainmentControlClient {

  private final WebSocket socket;
  private int requestIndex = 128;
  private final Map<Integer,BehaviorSubject<ApiResponse>> pendingRequests;


  public EntertainmentControlClient(WebSocket webSocket) {
    this.socket = webSocket;
    this.pendingRequests = new HashMap<>();
  }

  public void start() {
    socket.frameHandler(this::responseHandler);
    newRequest(
        new ClientRequest(requestIndex++, "getAvailableSources")
      );
  }

  public Observable<ApiResponse> newRequest(ClientRequest request) {
    // create an Observable
    BehaviorSubject<ApiResponse> responseChannel = BehaviorSubject.create();

    // queue it by its requestId
    pendingRequests.put(request.id, responseChannel);

    // send the request
    socket.writeFinalTextFrame(Json.encodePrettily(request));

    // return the Observable
    return responseChannel.hide();
  }

  private void responseHandler(WebSocketFrame frame) {

    // log, if anybody wants to see our private data
    System.out.println(frame.isText()
        ? frame.textData()
        : "Binary frame, length() = " + frame.binaryData().length());

    try {

      // jsonify frame -> get ApiResponse object
      ApiResponse response = frame.binaryData().toJsonObject().mapTo(ApiResponse.class);

      // lookup the responseChannel from the /pending/ map
      BehaviorSubject<ApiResponse> responseChannel = pendingRequests.get(response.id);

      // notify its subscribers
      if (responseChannel != null)
        responseChannel.onNext(response);

      // if the response was an error or completion, notify its subscribers
      switch (response.response) {

      }

    } catch (DecodeException | IllegalArgumentException e) {

      System.err.println("Received malformed response from server, discarding.\n>> " +
              e.getMessage());
//      e.printStackTrace();

    }

  }

}
