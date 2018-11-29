package de.eso.rxplayer;

import io.reactivex.schedulers.Schedulers;

public final class myEntertainmentService {
  private static final EntertainmentService es = new EntertainmentService(Schedulers.computation());

  public static EntertainmentService getEntertainmentService() {
    return es;
  }
}
