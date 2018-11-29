package de.eso.rxplayer.api.impl;

import de.eso.rxplayer.api.ApiRequest;
import de.eso.rxplayer.api.ApiResponse;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Responder<E> implements Observer<E> {

  private final ApiRequest request;
  private final Consumer<ApiResponse<E>> responseHandler;
  private Disposable resourceSubscription;

  public Responder(ApiRequest request, Consumer<ApiResponse<E>> responseHandler) {
    this.request = request;
    this.responseHandler = responseHandler;
  }

  @Override
  public void onSubscribe(Disposable d) {
    this.resourceSubscription = d;
  }

  @Override
  public void onNext(E o) {
    // jsonify the object
    // write it to a frame
    responseHandler.accept(
        new ApiResponse<E>(
            request,
            ApiResponse.Type.SUCCESS,
            new ArrayList<E>(){{
              add(o);
            }}));
  }

  @Override
  public void onError(Throwable e) {
    // do i need to dispose of my subscription when the observable has failed?
    // go ask somebody..
    resourceSubscription.dispose();
    responseHandler.accept(
        new ApiResponse<E>(
            request,
            ApiResponse.Type.ERROR,
            null));
    e.printStackTrace();
    throw new AssertionError("Encountered unhandled Throwable " + e);
  }

  @Override
  public void onComplete() {
    responseHandler.accept(new ApiResponse<E>(request, ApiResponse.Type.COMPLETION, null));
    resourceSubscription.dispose();
  }
}
