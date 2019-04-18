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
//
//        Observable.just(songsLiveData.getValue())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(value -> playerConnectionManager.updateSongsCollection(songsLiveData.getValue()));
    }

    public MutableLiveData<List<Song>> getSongsLiveData() {
        return songsLiveData;
    }

    public void choseSongToPlay(Song song) {
        int songPosition = songsLiveData.getValue().indexOf(song);
        playerConnectionManager.playSelectedSong(songPosition);
    }

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
        List<Song> songsWithUniqueArtist = Observable.fromIterable(songs)
                                                     .distinct(item -> item.getArtist())
                                                     .toList()
                                                     .blockingGet();

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
