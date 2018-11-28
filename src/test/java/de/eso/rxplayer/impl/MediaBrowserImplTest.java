package de.eso.rxplayer.impl;

import de.eso.rxplayer.Station;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


class MediaBrowserImplTest {

    @Test
    void getAvailableSources() {
    }

    @Test
    void getGlobalSearchScope() {
    }

    @Test
    void getAlbums() {
    }

    @Test
    void searchAlbum() {
    }

    @Test
    void searchTrack() {
    }

    @Test
    void getAlbumTracks() {
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
}