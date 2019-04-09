package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.media.Image;
import android.support.annotation.NonNull;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.SongChangedCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;

import javax.inject.Inject;

public class SongStatusViewModel extends AndroidViewModel implements SongChangedCallback {

    public Image albumCover;

    public ObservableField<String> title = new ObservableField<>("");

    public ObservableField<String> artist = new ObservableField<>("");

    @Inject
    PlayerConnectionManager playerConnectionManager;

    public SongStatusViewModel(@NonNull Application application) {
        super(application);
        //TODO check if this is needed
        ((App)application).playerComponent.injectPlayerProviderComponent(this);
        playerConnectionManager.setSongChangedCallback(this);
        displayCurrentSong();
    }

    @Override
    public void receiveNewCurrentSong(Song song) {
        title.set(song.getTitle());
        artist.set(song.getArtist());
    }

    @Override
    protected void onCleared() {
        playerConnectionManager.removeSongChangedCallback(this);
        super.onCleared();
    }

    private void displayCurrentSong() {
        Song currentSong = playerConnectionManager.getCurrentSong();

        if(currentSong != null) {
            title.set(currentSong.getTitle());
            artist.set(currentSong.getArtist());
        }
    }
}
