package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;


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
