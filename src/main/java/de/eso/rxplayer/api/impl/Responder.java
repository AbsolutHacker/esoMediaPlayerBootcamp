package de.eso.rxplayer.api.impl;

import de.eso.rxplayer.api.ApiRequest;
import de.eso.rxplayer.api.ApiResponse;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.HashMap;
import java.util.function.Consumer;

public class Responder implements Observer {

  private final ApiRequest request;
  private final Consumer<ApiResponse> responseHandler;

  public Responder(ApiRequest request, Consumer<ApiResponse> responseHandler) {
    this.request = request;
    this.responseHandler = responseHandler;
  }

  @Override
  public void onSubscribe(Disposable d) {

  }

  @Override
  public void onNext(Object o) {
    // jsonify the object
    // write it to a frame
    responseHandler.accept(
        new ApiResponse(
            request,
            ApiResponse.Type.SUCCESS,
            new HashMap<String,Object>() {{
              put("timer_id", o);
            }}
        )
    );
  }

  @Override
  public void onError(Throwable e) {
    e.printStackTrace();
    throw new AssertionError("Encountered unhandled Throwable " + e);
  }

  @Override
  public void onComplete() {

  }
}
