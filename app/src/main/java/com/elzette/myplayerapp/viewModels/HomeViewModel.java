package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.BoundToServiceCallback;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.services.PlayerService;

import javax.inject.Inject;

public class HomeViewModel extends AndroidViewModel implements IsMusicPlayingCallback {

    private boolean isMusicPlaying;

    @Inject
    PlayerConnectionManager playerConnectionManager;

    @Inject
    MusicFileSystemScanner fileSystemScanner;

    public HomeViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerConnectionManager.setIsMusicPlayingCallback(this);
    }

    public void loadMusicData() {
        fileSystemScanner.loadSongs();
    }

    public void pausePlayback() {
        if(isMusicPlaying) {
            playerConnectionManager.pauseMedia();
        }
    }

    public void resumePlayback() {
        if(!isMusicPlaying) {
            playerConnectionManager.playMedia();
        }
    }

    @Override
    public void changeMusicPlaybackState(boolean isPlaying) {
        isMusicPlaying = isPlaying;
    }

    @Override
    protected void onCleared() {
        playerConnectionManager.removeIsMusicPlayingCallback(this);
        super.onCleared();
    }
}
