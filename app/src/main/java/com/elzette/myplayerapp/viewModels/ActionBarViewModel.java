package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.providers.IsMusicEmptyCallback;
import com.elzette.myplayerapp.providers.PlayerProvider;

import javax.inject.Inject;

public class ActionBarViewModel extends AndroidViewModel implements IsMusicEmptyCallback {

    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public boolean canPlayMusic = true;

    @Inject
    PlayerProvider playerProvider;

    public ActionBarViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        playerProvider.getSongs().setCollectionEmptySubscribers(this);
    }

    public void onPlayClick() {
        playerProvider.play();
        isPlaying.set(true);
    }

    public void onPauseClick() {
        playerProvider.pause();
        isPlaying.set(false);
    }

    public void onNextClick() {
        playerProvider.playNextSong();
    }

    public void onPrevClick() {
        playerProvider.playPrevSong();
    }

    @Override
    public void isMusicCollectionEmptyChanged(boolean isEmpty) {
        canPlayMusic = !isEmpty;
    }
}
