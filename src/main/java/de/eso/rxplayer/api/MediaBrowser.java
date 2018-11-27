package de.eso.rxplayer.api;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Station;
import de.eso.rxplayer.Track;

import java.util.List;

public interface MediaBrowser {
  // global search methods
  List<Album> getAlbums();
  List<Track> searchTrack(String name);

  List<Track> getAlbumTracks(Album album);
  List<Station> getStations();
}
