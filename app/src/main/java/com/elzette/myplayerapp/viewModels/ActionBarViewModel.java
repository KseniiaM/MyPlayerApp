package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.Helpers.PlayerProvider;

import javax.inject.Inject;

public class ActionBarViewModel extends AndroidViewModel {

    public ObservableBoolean isPlaying = new ObservableBoolean(false);

    @Inject
    PlayerProvider playerProvider;

    public ActionBarViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
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
}
