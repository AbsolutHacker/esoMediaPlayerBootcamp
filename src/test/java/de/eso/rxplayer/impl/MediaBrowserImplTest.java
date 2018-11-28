package de.eso.rxplayer.impl;

import de.eso.rxplayer.Album;
import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Station;
import de.eso.rxplayer.Track;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


class MediaBrowserImplTest {

    @Test
    void getAvailableSources() {
        List<Audio.Connection> availableSources = new ArrayList<>();
        availableSources.add(Audio.Connection.CD);
        availableSources.add(Audio.Connection.USB);

        MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();
        List<Audio.Connection> sources = mediaBrowser.getAvailableSources();
        sources.forEach(source -> {
            assertTrue(availableSources.contains(source));
            availableSources.remove(source);
        });

        assertTrue(availableSources.size() == 0);
    }

    @Test
    void getAlbums() {
        List<Album> availableAlbums = getAvailableAlbums();
        MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

        Observable<List<Album>> albums$ = mediaBrowser.getAlbums();
        albums$.subscribe(albums -> albums.forEach(album -> assertTrue(availableAlbums.contains(album))));
    }

    @Test
    void searchAlbum() {
        List<Album> availableAlbums = getAvailableAlbums();
        MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

        Observable<List<Album>> albums$ = mediaBrowser.searchAlbum("Fetty Wap");
        albums$.subscribe(albums -> albums.forEach(album -> assertTrue(availableAlbums.contains(album))));
    }

    @Test
    void searchTrack() {
        List<Track> availableTracks = getAvailAbleTracks();
        MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

        Observable<List<Track>> tracks$ = mediaBrowser.searchTrack("Trap Queen");
        tracks$.subscribe(tracks -> tracks.forEach(track -> assertTrue(availableTracks.contains(track))));
    }

    @Test
    void getAlbumTracks() {
        List<Track> availableTracks = getAvailAbleTracks();
        MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();

        Observable<List<Track>> tracks$ = mediaBrowser.getAlbumTracks(getAvailableAlbums().get(0));
        tracks$.subscribe(tracks -> tracks.forEach(track -> assertTrue(availableTracks.contains(track))));
    }

    @Test
    void getStations() {
        List<Station> knownStations = new ArrayList<>();

        MediaBrowserImpl mediaBrowser = MediaBrowserImpl.getInstance();
        mediaBrowser.getStations().subscribe(stations -> stations.forEach(station -> {
            assertTrue(knownStations.contains(station));
            knownStations.remove(station); //remove the found station
        }));

        assertTrue(knownStations.size() == 0); //every known station should have been found and deleted
    }

    @NotNull
    private List<Track> getAvailAbleTracks() {
        List<Track> availableTracks = new ArrayList<>();
        availableTracks.add(new Track(
                1,
                1,
                6,
                "Trap Queen",
                222,
                "2017-08-21T06:20:35.799Z",
                "2017-08-21T06:20:35.799Z"
        ));

        return availableTracks;
    }

    @NotNull
    private List<Album> getAvailableAlbums() {
        List<Album> availableAlbums = new ArrayList<>();
        availableAlbums.add(new Album(
                1,
                "Fetty Wap",
                6,
                "https://api.deezer.com/album/11227614/image",
                "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/56x56-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/250x250-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/500x500-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/cover/f14becbb0d888cd5457d18d3bb670731/1000x1000-000000-80-0-0.jpg",
                "2017-08-21T06:20:32.220Z",
                "2017-08-21T06:20:32.220Z"
        ));

        return availableAlbums;
    }
}