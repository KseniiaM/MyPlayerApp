package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.BoundToServiceCallback;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;

public class SongListViewModel extends AndroidViewModel implements
                                       UpdateCollectionCallback,
                                       BoundToServiceCallback {

    private static final String TAG = SongListViewModel.class.getSimpleName();

    private MutableLiveData<List<Song>> songsLiveData;

    @Inject
    PlayerConnectionManager playerConnectionManager;

    @Inject
    MusicFileSystemScanner scanner;

    public SongListViewModel(Application app) {
        super(app);
        //TODO check if this is needed
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerConnectionManager.setBoundToServiceCallbacks(this);
        getSongsLiveData().setValue(scanner.getSongs());

    }

    public MutableLiveData<List<Song>> getSongsLiveData() {
        if (songsLiveData == null) {
            songsLiveData = new MutableLiveData<List<Song>>();
        }
        return songsLiveData;
    }

    public void choseSongToPlay(int position) {
        playerConnectionManager.playSelectedSong(position);
    }

    @Override
    public void onCollectionUpdated(List collection) {
        songsLiveData.setValue(collection);
    }

    @Override
    protected void onCleared() {
        scanner.removeUpdateCollectionCallback(this);
        playerConnectionManager.removeBoundToServiceCallbacks(this);
        super.onCleared();
    }

    private void initCallbacks() {
        ObservableArrayList<Song> songs = scanner.getSongs();
        //getSongsLiveData().setValue(songs);
    }

    @Override
    public void onBoundToService() {
        initCallbacks();
    }
}
