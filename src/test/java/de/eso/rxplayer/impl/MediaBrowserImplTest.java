package de.eso.rxplayer.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Station;
import de.eso.rxplayer.Track;
import io.reactivex.observers.TestObserver;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class MediaBrowserImplTest {

  @Test
  void getAvailableSources() {
    List<Audio.Connection> availableSources = new ArrayList<>();
    availableSources.add(Audio.Connection.CD);
    availableSources.add(Audio.Connection.USB);

    MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();
    List<Audio.Connection> sources = mediaBrowser.getAvailableSources();
    sources.forEach(
        source -> {
          assertTrue(availableSources.contains(source));
          availableSources.remove(source);
        });

    assertTrue(availableSources.size() == 0);
  }

  @Test
  void getAlbums() throws InterruptedException {
    MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

    TestObserver<List<Album>> albums$ = mediaBrowser.getAlbums().test();
    Thread.sleep(2000);
    albums$.assertValueAt(0, list -> list.contains(getAvailableAlbums().get(0)));
  }

  @Test
  void searchAlbum() throws InterruptedException {
    MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

    TestObserver<List<Album>> albums$ = mediaBrowser.searchAlbum("Fetty Wap").test();
    Thread.sleep(2000);
    albums$.assertValueAt(0, albums -> albums.get(0).equals(getAvailableAlbums().get(0)));
  }

  @Test
  void searchTrack() throws InterruptedException {
    MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

    TestObserver<List<Track>> tracks$ = mediaBrowser.searchTrack("Trap Queen").test();
    Thread.sleep(2000);
    tracks$.assertValueAt(0, tracks -> tracks.get(0).equals(getAvailAbleTracks().get(0)));
  }

  @Test
  void getAlbumTracks() throws InterruptedException {
    MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

    TestObserver<List<Track>> tracks$ =
        mediaBrowser.getAlbumTracks(getAvailableAlbums().get(0)).test();
    Thread.sleep(2000);
    tracks$.assertValueAt(0, tracks -> tracks.get(0).equals(getAvailAbleTracks().get(0)));
  }

  @Test
  void getStations() throws InterruptedException {
    List<Station> knownStations =
        new ArrayList<>(); // radio was never built in, we don't know a single station to test
    MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

    TestObserver<List<Station>> stations$ = mediaBrowser.getStations().test();
    Thread.sleep(1000);

    if (!mediaBrowser
        .getGlobalSearchScope()
        .contains(Audio.Connection.RADIO)) { // no radio built in
      stations$.assertNoValues();

    } else {
      stations$.assertValueAt(
          0, stations -> stations.size() == 0 || stations.get(0).equals(knownStations.get(0)));
    }
  }

  @NotNull
  private List<Track> getAvailAbleTracks() {
    List<Track> availableTracks = new ArrayList<>();
    availableTracks.add(
        new Track(
            1, 1, 6, "Trap Queen", 222, "2017-08-21T06:20:35.799Z", "2017-08-21T06:20:35.799Z"));

    return availableTracks;
  }

  @NotNull
  private List<Album> getAvailableAlbums() {
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
