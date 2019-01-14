package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.providers.PlayerProvider;

import javax.inject.Inject;

public class HomeViewModel extends AndroidViewModel {

    @Inject
    PlayerProvider playerProvider;

    public HomeViewModel(Application app) {
        super(app);
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
    }

    public void loadMusicData() {
        playerProvider.loadSongs();
    }
}
