package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.api.MediaBrowser;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Observable<List<Album>> getAlbums(Set<Audio.Connection> searchScope) {
        Observable<List<Track>> tracks$ = getAllTracksInScope(searchScope);
        return getAlbumsFromTracks(tracks$);
    }

    @Override
    public Observable<List<Album>> searchAlbum(String name, Set<Audio.Connection> searchScope) {
        Observable<List<Track>> tracks$ = getAllTracksInScope(searchScope);
        Observable<List<Album>> albums$ = getAlbumsFromTracks(tracks$);

        Observable<List<Album>> searchedAlbums = albums$
                .map(albums -> albums.stream()
                        .filter(album -> album.getName().equals(name))
                        .collect(Collectors.toList())
                );

        return searchedAlbums;
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
        Observable<List<Station>> list$ = es.getFm().list();
        return null;
    }



    private Observable<List<Track>> getAllTracksInScope(Set<Audio.Connection> searchScope) {
        Observable<List<Track>> usbTracks$ = null;
        Observable<List<Track>> cdTracks$ = null;
        Observable<Track> radioTrack$ = null;

        for (Audio.Connection scope : searchScope) {
            switch (scope) {
                case USB:
                    usbTracks$ = es.getUsb().list();
                    break;
                case CD:
                    cdTracks$ = es.getCd().list();
                    break;
                case RADIO:
                    radioTrack$ = es.getFm().radioText(); //get currently playing track
                    break;
            }
        }


        if (usbTracks$ == null) {
            usbTracks$ = Observable.empty();
        }
        if (cdTracks$ == null) {
            cdTracks$ = Observable.empty();
        }
        if (radioTrack$ == null) {
            radioTrack$ = Observable.empty();
        }

        Observable<List<Track>> radioTrackList = radioTrack$.map(track -> {
            List<Track> tmpList = new ArrayList<>();
            tmpList.add(track);
            return tmpList;
        });

        return Observable.concat(usbTracks$, cdTracks$, radioTrackList);
    }


    private Observable<List<Album>> getAlbumsFromTracks(Observable<List<Track>> obsTracks$) {
        Observable<List<Integer>> albumIds$ = obsTracks$
                .map(tracks -> tracks
                        .stream()
                        .map(Track::getAlbumId)
                        .distinct()
                        .collect(Collectors.toList())
                );

        //ToDo flatmap should probably be used here in some case
//        Observable<List<Single<Album>>> obsSingleAlbumList = albumIds
//                .map(values -> values.stream()
//                        .map(id -> es.getBrowser().albumById(id))
//                        .collect(Collectors.toList())
//                );
//
//        obsSingleAlbumList
//                .map(singles -> singles.stream()
//                        .map(albumSingle -> {
//                            Observable<Album> albumObservable = albumSingle.toObservable();
//                        })
//                );


        List<Album> albumList = new ArrayList<>();

        albumIds$.forEach(value -> value.forEach(id -> {
                    Single<Album> albumSingle$ = es.getBrowser().albumById(id);
                    albumSingle$.subscribe((Consumer<Album>) albumList::add);
                })
        );

        return Observable.fromArray(albumList);
    }
}
