package de.eso.rxplayer.impl;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Track;
import io.reactivex.observers.TestObserver;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MediaBrowserTest {

  @Test
  void test_getAlbums() throws InterruptedException {
    TestObserver<List<Album>> testObs =
        MediaBrowserImpl.getInstance()
            .getAlbums()
            .test()
            .assertValue(list -> list.contains(getAvailableAlbum().get(0)));
    Thread.sleep(5000);
  }

  private List<Track> getAvailAbleTrack() {
    List<Track> availableTracks = new ArrayList<>();
    availableTracks.add(
        new Track(
            1, 1, 6, "Trap Queen", 222, "2017-08-21T06:20:35.799Z", "2017-08-21T06:20:35.799Z"));

    return availableTracks;
  }

  private List<Album> getAvailableAlbum() {
    List<Album> availableAlbums = new ArrayList<>();
    availableAlbums.add(
        new Album(
            1,
            "Fetty Wap",
            6,
            "https://api.deezer.com/album/11227614/image",
            "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/56x56-000000-80-0-0.jpg",
            "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/250x250-000000-80-0-0.jpg",
            "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/500x500-000000-80-0-0.jpg",
            "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/1000x1000-000000-80-0-0.jpg",
            "2017-08-21T06:20:32.220Z",
            "2017-08-21T06:20:32.220Z"));

    return availableAlbums;
  }
}
