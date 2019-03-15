package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.models.AlbumModel;
import com.elzette.myplayerapp.models.ArtistModel;
import com.elzette.myplayerapp.models.MusicItemBaseModel;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


//TODO use livedata from db here
public class SongListViewModel extends AndroidViewModel implements
                                       UpdateCollectionCallback {

    private static final String TAG = SongListViewModel.class.getSimpleName();

    private MutableLiveData<List<Song>> songsLiveData = new MutableLiveData<>();

    @Inject
    PlayerConnectionManager playerConnectionManager;

    @Inject
    MusicFileSystemScanner scanner;

    public SongListViewModel(Application app) {
        super(app);
        //TODO check if this is needed
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        getSongsLiveData().setValue(scanner.getSongs());

    }

    public MutableLiveData<List<Song>> getSongsLiveData() {
        return songsLiveData;
    }

    public void choseSongToPlay(int position) {
        playerConnectionManager.playSelectedSong(position);
    }

    public void choseSongToPlay(Song song) {
        int songPosition = songsLiveData.getValue().indexOf(song);
        playerConnectionManager.playSelectedSong(songPosition);
    }
//
//    public List<? extends MusicItemBaseModel> getFormattedSongList(int itemId) {
//        switch (itemId) {
//            case 1:
//                return getSongsLiveData().getValue();
//            case 2:
//                return getAlbums(getSongsLiveData().getValue());
//            case 3:
//                //return getArtists(getSongsLiveData().getValue());
//                return getArtists(getSongsLiveData().getValue());
//            default:
//                return new ArrayList<>();
//        }
//    }

    public List<Song> getSongsForArtist(ArtistModel artist) {
        return Observable.fromIterable(songsLiveData.getValue())
                         .filter(item -> item.getArtist().equals(artist.getArtist()))
                         .toList()
                         .blockingGet();
    }

    public List<Song> getSongsForAlbum(AlbumModel album) {
        return Observable.fromIterable(songsLiveData.getValue())
                .filter(item -> item.getAlbum().equals(album.getAlbum()))
                .toList()
                .blockingGet();
    }

//    private List<ArtistModel> getArtists(List<Song> songs) {
//        return Observable.fromIterable(songs)
//                .groupBy(item -> item.getArtist())
//                .map(item -> {
//                    Song firstSong = item.firstElement().blockingGet();
//                    long itemsCount = item.count().blockingGet();
//                    return new ArtistModel(firstSong.getArtist(), itemsCount);
//                })
//                .toList()
//                .blockingGet();
//    }
//
//    private List<AlbumModel> getAlbums(List<Song> songs) {
//        Log.d(TAG, "thread before " + Thread.currentThread().getId());
//        List<AlbumModel> m = new ArrayList<>();
//
//        //List<Song> s = songs.stream().collect(Collectors.groupingBy(Song::getAlbum)).;
//
//                Observable.fromIterable(songs)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                         .groupBy(item -> item.getAlbum())
//                         .map(item -> {
//                             Log.d(TAG, "thread inside " + Thread.currentThread().getId());
//                             Song firstSong = item.firstElement().blockingGet();
//                             long itemsCount = item.count().blockingGet();
//                             return new AlbumModel(firstSong.getArtist(), firstSong.getAlbum(), itemsCount);
//                         }).doOnNext(item -> m.add(0, item));
//        Log.d(TAG, "thread after " + Thread.currentThread().getId());
//        return m;
//    }

    public List<AlbumModel> getAlbums() {
        List<Song> songs = getSongsLiveData().getValue();
        List<AlbumModel> albums = new ArrayList<>();
        long songCount;
        List<Song> songsWithUniqueAlbum = Observable.fromIterable(songs).distinct(item -> item.getAlbum()).toList().blockingGet();

        for (Song song: songsWithUniqueAlbum) {
            songCount = songs.stream()
                             .filter(songItem -> songItem.getAlbum().equals(song.getAlbum()))
                             .count();
            albums.add(new AlbumModel(song.getArtist(), song.getAlbum(), songCount));
        }
        return albums;
    }

    public List<ArtistModel> getArtists() {
        List<Song> songs = getSongsLiveData().getValue();
        List<ArtistModel> artists = new ArrayList<>();
        long songCount;
        List<Song> songsWithUniqueArtist = Observable.fromIterable(songs).distinct(item -> item.getArtist()).toList().blockingGet();

        for (Song song: songsWithUniqueArtist) {
            songCount = songs.stream()
                    .filter(songItem -> songItem.getArtist().equals(song.getArtist()))
                    .count();
            artists.add(new ArtistModel(song.getArtist(), songCount));
        }
        return artists;
    }

    @Override
    public void onCollectionUpdated(List collection) {
        songsLiveData.setValue(collection);
    }

    @Override
    protected void onCleared() {
        scanner.removeUpdateCollectionCallback(this);
        super.onCleared();
    }
}
