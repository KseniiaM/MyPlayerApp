package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.providers.PlayerProvider;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;

public class ActionBarViewModel extends AndroidViewModel implements UpdateCollectionCallback, IsMusicPlayingCallback {

    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public boolean canPlayMusic = false;

    @Inject
    PlayerProvider playerProvider;

    public ActionBarViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerProvider.getSongs().setCollectionUpdateSubscribers(this);
        playerProvider.isMusicPlayingCallback = this;
    }

    public void onPlayClick() {
        playerProvider.play();
    }

    public void onPauseClick() {
        playerProvider.pause();
    }

    public void onNextClick() {
        playerProvider.playNextSong();
    }

    public void onPrevClick() {
        playerProvider.playPrevSong();
    }

    @Override
    public void onCollectionUpdated(List collection) {
        canPlayMusic = !(collection == null || collection.isEmpty());
    }

    @Override
    public void isMusicPlaying(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
    }
}
