package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.Helpers.PlayerProvider;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.di.ContextModule;
import com.elzette.myplayerapp.di.DaggerDatabaseComponent;
import com.elzette.myplayerapp.di.DatabaseComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SongListViewModel extends AndroidViewModel {

    private static final String TAG = SongListViewModel.class.getSimpleName();

    private MutableLiveData<List<Song>> songsLiveData;

    @Inject
    PlayerProvider playerProvider;

    public SongListViewModel(Application app) {
        super(app);

        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        getSongsLiveData().setValue(playerProvider.getSongs());
    }

    public MutableLiveData<List<Song>> getSongsLiveData() {
        if (songsLiveData == null) {
            songsLiveData = new MutableLiveData<List<Song>>();
        }
        return songsLiveData;
    }

    public void choseSongToPlay(int position) {
        playerProvider.playSelectedSong(position);
    }
}
