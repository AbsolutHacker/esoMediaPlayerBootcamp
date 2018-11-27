package de.eso.rxplayer.vertx;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Entertainment;
import de.eso.rxplayer.EntertainmentService;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class Launcher {
  public static void main(String[] args) {
    Entertainment entertainmentSystem = new EntertainmentService(Schedulers.single());
    EntertainmentControlServer serverInstance = new EntertainmentControlServer(entertainmentSystem);
    serverInstance.start();


    System.out.println("Entry Launcher::someStuff");
    entertainmentSystem.getAudio().observe(Audio.Connection.CD).doOnError(System.out::println);
    System.out.println("installed error handler on CDConnection");
    try {
      Thread.sleep(3000);
      entertainmentSystem.getCd().select(0)
          .andThen(entertainmentSystem.getCd().play())
      .subscribe(() -> System.out.println("Started to play Track #0 on CD"));
    } catch (InterruptedException e) {
      // eat it
      System.out.println("InterruptedException in Launcher::main");
    }
    entertainmentSystem.getAudio().start(Audio.Connection.CD);
  }

}
