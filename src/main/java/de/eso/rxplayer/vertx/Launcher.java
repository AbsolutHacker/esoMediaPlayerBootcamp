package de.eso.rxplayer.vertx;

import de.eso.rxplayer.impl.MediaBrowserImpl;
import de.eso.rxplayer.impl.MediaPlayerImpl;

public class Launcher {

  public static void main(String[] args) {

    EntertainmentControlServer serverInstance = new EntertainmentControlServer(
        MediaPlayerImpl.getInstance(),
        MediaBrowserImpl.getInstance());
    serverInstance.start();

  }

}
