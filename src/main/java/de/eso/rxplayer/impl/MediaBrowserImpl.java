package de.eso.rxplayer.impl;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Station;
import de.eso.rxplayer.Track;
import de.eso.rxplayer.api.MediaBrowser;

import java.util.List;
import java.util.Set;

/**
 * Main Access point for getting the data for the MediaBrowser
 */
public class MediaBrowserImpl implements MediaBrowser{
    @Override
    public List<Audio.Connection> getSources() {
        return null;
    }

    @Override
    public List<Album> getAlbums(Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public List<Album> searchAlbum(String name, Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public List<Track> searchTrack(String name, Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public List<Track> getAlbumTracks(Album album) {
        return null;
    }

    @Override
    public List<Station> getStations() {
        return null;
    }
}
