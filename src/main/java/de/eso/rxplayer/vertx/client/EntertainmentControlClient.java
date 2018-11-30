package de.eso.rxplayer.vertx.client;

import de.eso.rxplayer.api.ApiResponse;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import org.jetbrains.annotations.NotNull;

public class EntertainmentControlClient {

  private final WebSocket socket;
  private int requestIndex = 128;
  private final Map<Integer, BehaviorSubject<ApiResponse>> pendingRequests;

  public EntertainmentControlClient(WebSocket webSocket) {
    this.socket = webSocket;
    this.pendingRequests = new HashMap<>();
  }

  public void start() {
    socket.frameHandler(this::responseHandler);
  }

  public Observable<ApiResponse> request(ClientRequest request) {
    // create an Observable
    BehaviorSubject<ApiResponse> responseChannel = BehaviorSubject.create();

    // queue it by its requestId
    pendingRequests.put(request.id, responseChannel);

    // send the request
    socket.writeFinalTextFrame(Json.encodePrettily(request));

    // return the Observable
    return responseChannel.hide();
  }

  public Observable<ApiResponse> newRequest(
      String singleMethodToInvoke, Class<?> expectedReturnType) {
    return request(new ClientRequest(requestIndex++, singleMethodToInvoke, expectedReturnType));
  }

  private void responseHandler(@NotNull WebSocketFrame frame) {

    try {

      System.out.println(frame.binaryData());
      // jsonify frame -> get ApiResponse object
      ApiResponse response = frame.binaryData().toJsonObject().mapTo(ApiResponse.class);

      // lookup the responseChannel from the "pending" map
      BehaviorSubject<ApiResponse> responseChannel = pendingRequests.get(response.getId());

      // notify the response's subscribers if the request was successful;
      // if it was an error or completion, notify its subscribers and discard
      // the response channel object
      if (null != responseChannel) {
        switch (response.getResponseType()) {
          case ERROR:
            responseChannel.onError(new RejectedExecutionException(response.getBody().toString()));
            pendingRequests.remove(response.getId());
            break;
          case COMPLETION:
            responseChannel.onComplete();
            pendingRequests.remove(response.getId());
            break;
          case SUCCESS:
            responseChannel.onNext(response);
            break;
          default:
        }
      }

    } catch (DecodeException | IllegalArgumentException e) {

      System.err.println(
          "Received malformed response from server, discarding.\n>> " + e.getMessage());
      //      e.printStackTrace();

    }
  }
}
