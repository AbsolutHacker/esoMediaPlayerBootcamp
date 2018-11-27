package de.eso.rxplayer.api;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Station;
import de.eso.rxplayer.Track;

import java.util.EnumSet;
import java.util.List;

public interface MediaBrowser {
  EnumSet<Audio.Connection> globalSS = EnumSet.allOf(Audio.Connection.class);

  // global search methods
  List<Audio.Connection> getSources();
  default List<Album> getAlbums() {
    return getAlbums(globalSS);
  }
  List<Album> getAlbums(EnumSet<Audio.Connection> searchScope);

  default List<Album> searchAlbum(String name) {
    return searchAlbum(name, globalSS);
  }
  List<Album> searchAlbum(String name, EnumSet<Audio.Connection> searchScope);
  default List<Track> searchTrack(String name) {
    return searchTrack(name, globalSS);
  }
  List<Track> searchTrack(String name, EnumSet<Audio.Connection> searchScope);

  void setSearchScope(EnumSet<Audio.Connection> sourcesInScope);

  // source: any, context/scope: given album
  List<Track> getAlbumTracks(Album album);

  // source: radio
  List<Station> getStations();
}
