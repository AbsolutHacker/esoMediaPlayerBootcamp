package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.api.MediaBrowser;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.EnumSet;
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
    public List<Audio.Connection> getAvailableSources() {
        ArrayList<Audio.Connection> connections = new ArrayList<>();
        connections.add(Audio.Connection.CD);
        connections.add(Audio.Connection.USB);
        //connections.add(Audio.Connection.RADIO); Radio is not built in in this model

        return connections;
    }

    /**
     * Overwrite the GlobalSearchScope as Radio is not built into this model
     * @return the global SearchScope
     */
    @Override
    public Set<Audio.Connection> getGlobalSearchScope() {
        EnumSet<Audio.Connection> globalSS = EnumSet.allOf(Audio.Connection.class);
        globalSS.remove(Audio.Connection.RADIO);
        return globalSS;
    }

    @Override
    public Observable<Album> getAlbums(Set<Audio.Connection> searchScope) {

        Observable<Track> allTracks = getAllTracksInScope(searchScope);

        Observable<Integer> albumIds = allTracks
                .map(Track::getAlbumId)
                .distinct();

        return albumIds.flatMapSingle(id -> es.getBrowser().albumById(id));
    }

    @Override
    public Observable<Album> searchAlbum(String name, Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public Observable<Track> searchTrack(String name, Set<Audio.Connection> searchScope) {
        return null;
    }

    @Override
    public Observable<Track> getAlbumTracks(Album album) {
        return null;
    }

    @Override
    public Observable<Station> getStations() {

        return null;
    }



    private Observable<Track> getAllTracksInScope(Set<Audio.Connection> searchScope) {
        Observable<List<Track>> usbTracks = null;
        Observable<List<Track>> cdTracks = null;
        Observable<Track> radioTrack = null;

        for (Audio.Connection scope : searchScope) {
            switch (scope) {
                case USB:
                    usbTracks = es.getUsb().list();
                    break;
                case CD:
                    cdTracks = es.getCd().list();
                    break;
                case RADIO:
                    radioTrack = es.getFm().radioText(); //get currently playing track
                    break;
            }
        }


        if (usbTracks == null) {
            usbTracks = Observable.empty();
        }
        if (cdTracks == null) {
            cdTracks = Observable.empty();
        }
        if (radioTrack == null) {
            radioTrack = Observable.empty();
        }

        Observable<List<Track>> radioTrackList = radioTrack.map(track -> {
            List<Track> tmpList = new ArrayList<>();
            tmpList.add(track);
            return tmpList;
        });

        Observable<List<Track>> concat = Observable.concat(usbTracks, cdTracks, radioTrackList);

        return concat.flatMapIterable(track -> track);
    }
}
