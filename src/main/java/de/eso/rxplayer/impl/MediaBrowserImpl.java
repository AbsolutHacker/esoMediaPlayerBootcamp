package de.eso.rxplayer.impl;

import de.eso.rxplayer.*;
import de.eso.rxplayer.api.MediaBrowser;
import io.reactivex.Observable;
import io.reactivex.Single;
import kotlin.NotImplementedError;

import java.util.*;
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

        //filter from all albums the ones that have the same name as the one searched for
        Observable<List<Album>> searchedAlbums$ = albums$
                .map(albums -> albums.stream()
                        .filter(album -> album.getName().equals(name))
                        .collect(Collectors.toList())
                );

        return searchedAlbums$;
    }

    @Override
    public Observable<List<Track>> searchTrack(String name, Set<Audio.Connection> searchScope) {
        Observable<List<Track>> tracks$ = getAllTracksInScope(searchScope);

        //filter from all titles the ones that have the same name as the one searched for
        Observable<List<Track>> searchedTracks$ = tracks$
                .map(tracks -> tracks.stream()
                        .filter(track -> track.getTitle().equals(name))
                        .collect(Collectors.toList())
                );

        return searchedTracks$;
    }

    @Override
    public Observable<List<Track>> getAlbumTracks(Album album) {
        Observable<List<Track>> tracks$ = getAllTracksInScope(getGlobalSearchScope());

        //filter from all tracks the ones that have the same albumId as the album searched for
        Observable<List<Track>> albumTracks$ = tracks$.map(tracks -> tracks.stream()
                .filter(track -> track.getAlbumId() == album.getId())
                .collect(Collectors.toList())
        );

        return albumTracks$;
    }

    @Override
    public Observable<List<Station>> getStations() {
        Observable<List<Station>> stationList = null;
        try {
            Radio radio = es.getFm();
            stationList = radio.list();
        } catch (NotImplementedError error) {
            //radio is not built in
            stationList = Observable.empty();
        }
        return stationList;
    }



    private Observable<List<Track>> getAllTracksInScope(Set<Audio.Connection> searchScope) {
        Observable<List<Track>> usbTracks$ = null;
        Observable<List<Track>> cdTracks$ = null;
        Observable<Track> radioTrack$ = null;

        //fill the list for every type of connection -> possibly different set of tracks
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

        //if the lists are null because of any reason look at them as if they were empty
        if (usbTracks$ == null) {
            usbTracks$ = Observable.empty();
        }
        if (cdTracks$ == null) {
            cdTracks$ = Observable.empty();
        }
        if (radioTrack$ == null) {
            radioTrack$ = Observable.empty();
        }

        //radio always just has a single track
        Observable<List<Track>> radioTrackList$ = radioTrack$.map(track -> {
            List<Track> tmpList = new ArrayList<>();
            tmpList.add(track);
            return tmpList;
        });

        //concat all different sources together. Here the default information what came from where is lost
        return Observable.concat(usbTracks$, cdTracks$, radioTrackList$);
    }


    private Observable<List<Album>> getAlbumsFromTracks(Observable<List<Track>> tracks$) {
        //map all tracks into their AlbumIds (distinct)
        Observable<List<Integer>> albumIds$ = tracks$
                .map(tracks -> tracks
                        .stream()
                        .map(Track::getAlbumId)
                        .distinct()
                        .collect(Collectors.toList())
                );

        //map each Integer to it's album
        Observable<List<Album>> gottenAlbums = albumIds$.flatMapSingle(albumIds -> { //flatMapSINGLE
            List<Single<Album>> singleList = albumIds.stream()
                    .map(id -> es.getBrowser().albumById(id)) //get the Single<Album>
                    .collect(Collectors.toList());
            return Single.zip(singleList, array -> { //zip that Single<Album> back to Album
                Album[] albumArray = Arrays.stream(array).toArray(Album[]::new);
                List<Album> albums = Arrays.asList(albumArray);
                return albums;
            });
        });

        return gottenAlbums;
    }
}
