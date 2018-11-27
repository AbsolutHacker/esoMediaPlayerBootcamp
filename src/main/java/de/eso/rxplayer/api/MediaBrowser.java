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
  default Set<Audio.Connection> getGlobalSearchScope() {
    return EnumSet.allOf(Audio.Connection.class);
  }

  /**
   * Returns a #List list of all known audio sources.
   * These can be used to limit the scope of lookup functions
   * such as getAlbums() or searchTrack(String).
   * @return A list of all known audio sources.
   */
  List<Audio.Connection> getAvailableSources();


  /**
   * Returns a list of all available albums from all known sources.
   * @return A {@link List} of {@link Album}s
   */
  default Observable<Album> getAlbums() {
    return getAlbums(getGlobalSearchScope());
  }


  /**
   * Returns a list of albums available from the given source[s].
   * @return A {@link List} of {@link Album}s
   */
  Observable<Album> getAlbums(Set<Audio.Connection> searchScope);


    /**
     * [Comfort Function] <br/>
     * Calls #searchAlbum with the global searchScope - #getGlobalSearchScope()
     * @param name of the album to search
     * @return List of albums that were found
     */
  default Observable<Album> searchAlbum(String name) {
    return searchAlbum(name, getGlobalSearchScope());
  }


    /**
     * Search for all occurrences of an album inside the supplied connections
     * @param name of the album to search
     * @param searchScope Connections which to search through
     * @return List of albums that were found
     */
    Observable<Album> searchAlbum(String name, Set<Audio.Connection> searchScope);


    /**
     * [Comfort Function] <br/>
     * Calls #searchTrack with the global searchScope - #getGlobalSearchScope()
     * @param name of the track to search
     * @return List of tracks that were found
     */
  default Observable<Track> searchTrack(String name) {
    return searchTrack(name, getGlobalSearchScope());
  }


    /**
     * Search for all occurrences of a track inside the supplied connections
     * @param name of the track to search
     * @param searchScope Connections which to search through
     * @return List of tracks that were found
     */
    Observable<Track> searchTrack(String name, Set<Audio.Connection> searchScope);


    /**
     * Get all tracks of the supplied album
     * @param album to list the tracks of
     * @return List of Tracks from the supplied Album, Empty List if album null or empty
     */
    Observable<Track> getAlbumTracks(Album album);


    /**
     * Get all globally available radioStations
     * @return List of Stations, Empty List if no stations currently available
     */
    Observable<Station> getStations();
}
