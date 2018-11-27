package de.eso.rxplayer.api;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Station;
import de.eso.rxplayer.Track;
import io.reactivex.Observable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public interface MediaBrowser {
  Set<Audio.Connection> globalSS = EnumSet.allOf(Audio.Connection.class);


  /**
   * Returns a #List list of all known audio sources.
   * These can be used to limit the scope of lookup functions
   * such as getAlbums() or searchTrack(String).
   * @return A list of all known audio sources.
   */
  List<Audio.Connection> getSources();


  /**
   * Returns a list of all available albums from all known sources.
   * @return A {@link List} of {@link Album}s
   */
  default Observable<List<Album>> getAlbums() {
    return getAlbums(globalSS);
  }


  /**
   * Returns a list of albums available from the given source[s].
   * @return A {@link List} of {@link Album}s
   */
  Observable<List<Album>> getAlbums(Set<Audio.Connection> searchScope);


    /**
     * [Comfort Function] <br/>
     * Calls {@link searchAlbum} with the global searchScope - {@link globalSS}
     * @param name of the album to search
     * @return List of albums that were found
     */
  default Observable<List<Album>> searchAlbum(String name) {
    return searchAlbum(name, globalSS);
  }


    /**
     * Search for all occurrences of an album inside the supplied connections
     * @param name of the album to search
     * @param searchScope Connections which to search through
     * @return List of albums that were found
     */
    Observable<List<Album>> searchAlbum(String name, Set<Audio.Connection> searchScope);


    /**
     * [Comfort Function] <br/>
     * Calls {@link searchTrack} with the global searchScope - {@link globalSS}
     * @param name of the track to search
     * @return List of tracks that were found
     */
  default Observable<List<Track>> searchTrack(String name) {
    return searchTrack(name, globalSS);
  }


    /**
     * Search for all occurrences of a track inside the supplied connections
     * @param name of the track to search
     * @param searchScope Connections which to search through
     * @return List of tracks that were found
     */
    Observable<List<Track>> searchTrack(String name, Set<Audio.Connection> searchScope);


    /**
     * Get all tracks of the supplied album
     * @param album to list the tracks of
     * @return List of Tracks from the supplied Album, Empty List if album null or empty
     */
    Observable<List<Track>> getAlbumTracks(Album album);


    /**
     * Get all globally available radioStations
     * @return List of Stations, Empty List if no stations currently available
     */
    Observable<List<Station>> getStations();
}
