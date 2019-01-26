package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.BoundToServiceCallback;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;

public class ActionBarViewModel extends AndroidViewModel implements
                                        UpdateCollectionCallback,
                                        IsMusicPlayingCallback {

    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public boolean canPlayMusic = false;

    @Inject
    PlayerConnectionManager playerConnectionManager;

    @Inject
    MusicFileSystemScanner scanner;

    public ActionBarViewModel(Application app) {
        super(app);
        //TODO check if this is needed
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        scanner.setUpdateCollectionCallback(this);
        playerConnectionManager.setIsMusicPlayingCallback(this);
    }

    public void onPlayClick() {
        playerConnectionManager.playMedia();
    }

    public void onPauseClick() {
        playerConnectionManager.pauseMedia();
    }

    public void onNextClick() {
        playerConnectionManager.playNextSong();
    }

    public void onPrevClick() {
        playerConnectionManager.playPrevSong();
    }

    @Override
    public void onCollectionUpdated(List collection) {
        canPlayMusic = !(collection == null || collection.isEmpty());
    }

    @Override
    public void changeMusicPlaybackState(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
    }

    @Override
    protected void onCleared() {
        scanner.removeUpdateCollectionCallback(this);
        playerConnectionManager.removeIsMusicPlayingCallback(this);
        super.onCleared();
    }
}
