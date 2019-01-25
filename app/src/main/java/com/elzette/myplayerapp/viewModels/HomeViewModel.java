package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.providers.PlayerManager;

import javax.inject.Inject;

public class HomeViewModel extends AndroidViewModel implements IsMusicPlayingCallback {

    private boolean isMusicPlaying;

    @Inject
    PlayerManager playerManager;

    public HomeViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerManager.setIsMusicPlayingCallback(this);
    }

    public void loadMusicData() {
        playerManager.loadSongs();
    }

    public void pausePlayback() {
        if(isMusicPlaying) {
            playerManager.pause();
        }
    }

    public void resumePlayback() {
        if(!isMusicPlaying) {
            playerManager.play();
        }
    }

    @Override
    public void changeMusicPlaybackState(boolean isPlaying) {
        isMusicPlaying = isPlaying;
    }
}
