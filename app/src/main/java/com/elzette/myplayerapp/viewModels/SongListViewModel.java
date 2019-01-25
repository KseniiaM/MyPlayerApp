package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.providers.PlayerManager;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;

public class SongListViewModel extends AndroidViewModel implements UpdateCollectionCallback {

    private static final String TAG = SongListViewModel.class.getSimpleName();

    private MutableLiveData<List<Song>> songsLiveData;

    @Inject
    PlayerManager playerManager;

    public SongListViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerManager.getSongs().setCollectionUpdateSubscribers(this);
        getSongsLiveData().setValue(playerManager.getSongs().getValue());
    }

    public MutableLiveData<List<Song>> getSongsLiveData() {
        if (songsLiveData == null) {
            songsLiveData = new MutableLiveData<List<Song>>();
        }
        return songsLiveData;
    }

    public void choseSongToPlay(int position) {
        playerManager.playSelectedSong(position);
    }

    @Override
    public void onCollectionUpdated(List collection) {
        songsLiveData.setValue(collection);
    }

    @Override
    protected void onCleared() {
        playerManager.getSongs().removeFromCollectionUpdateSubscribers(this);
        super.onCleared();
    }
}
