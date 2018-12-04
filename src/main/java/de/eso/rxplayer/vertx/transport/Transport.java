package de.eso.rxplayer.vertx.transport;

import de.eso.rxplayer.api.ApiRequest;
import de.eso.rxplayer.api.ApiResponse;
import io.reactivex.Observable;

public interface Transport {
  void connect();
  void disconnect();
  <E> Observable<ApiResponse<E>> sendRequest(ApiRequest<E> request);
}
