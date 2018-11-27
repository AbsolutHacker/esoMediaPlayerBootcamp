package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.api.MediaBrowser;
import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

/**
 * Main Access point for getting the data for the MediaBrowser
 */
public final class MediaBrowserImpl implements MediaBrowser {

    private EntertainmentService es;
    private static MediaBrowserImpl mediaBrowser = new MediaBrowserImpl();

    private MediaBrowserImpl() {
        this.es = myEntertainmentService.getEntertainmentService();
    }

    public static MediaBrowserImpl getInstance() {
        return mediaBrowser;
    }

    @Override
    public List<Audio.Connection> getSources() {
        return null;
    }

    @Override
    public Observable<List<Album>> getAlbums(Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public Observable<List<Album>> searchAlbum(String name, Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public Observable<List<Track>> searchTrack(String name, Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public Observable<List<Track>> getAlbumTracks(Album album) {
        return null;
    }

    @Override
    public Observable<List<Station>> getStations() {
        return null;
    }
}
