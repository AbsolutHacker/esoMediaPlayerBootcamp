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

    /**
     * [Comfort Function] <br/>
     * Calls {@link searchAlbum} with the global searchScope - {@link globalSS}
     * @param name of the album to search
     * @return List of albums that were found
     */
  default List<Album> searchAlbum(String name) {
    return searchAlbum(name, globalSS);
  }

    /**
     * Search for all occurrences of an album inside the supplied connections
     * @param name of the album to search
     * @param searchScope Connections which to search through
     * @return List of albums that were found
     */
  List<Album> searchAlbum(String name, EnumSet<Audio.Connection> searchScope);

    /**
     * [Comfort Function] <br/>
     * Calls {@link searchTrack} with the global searchScope - {@link globalSS}
     * @param name of the track to search
     * @return List of tracks that were found
     */
  default List<Track> searchTrack(String name) {
    return searchTrack(name, globalSS);
  }

    /**
     * Search for all occurrences of a track inside the supplied connections
     * @param name of the track to search
     * @param searchScope Connections which to search through
     * @return List of tracks that were found
     */
  List<Track> searchTrack(String name, EnumSet<Audio.Connection> searchScope);

    /**
     * Get all tracks of the supplied album
     * @param album to list the tracks of
     * @return List of Tracks from the supplied Album, Empty List if album null or empty
     */
  List<Track> getAlbumTracks(Album album);

    /**
     * Get all globally available radioStations
     * @return List of Stations, Empty List if no stations currently available
     */
  List<Station> getStations();
}
