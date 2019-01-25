package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.providers.PlayerManager;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;

public class ActionBarViewModel extends AndroidViewModel implements UpdateCollectionCallback, IsMusicPlayingCallback {

    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public boolean canPlayMusic = false;

    @Inject
    PlayerManager playerManager;

    public ActionBarViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerManager.getSongs().setCollectionUpdateSubscribers(this);
        playerManager.setIsMusicPlayingCallback(this);
    }

    public void onPlayClick() {
        playerManager.play();
    }

    public void onPauseClick() {
        playerManager.pause();
    }

    public void onNextClick() {
        playerManager.playNextSong();
    }

    public void onPrevClick() {
        playerManager.playPrevSong();
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
        playerManager.getSongs().removeFromCollectionUpdateSubscribers(this);
        super.onCleared();
    }
}
