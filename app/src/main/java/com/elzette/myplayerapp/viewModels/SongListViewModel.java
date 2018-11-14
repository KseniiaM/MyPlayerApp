package com.elzette.myplayerapp.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;

import com.elzette.myplayerapp.models.SongModel;

import java.util.Date;

public class SongListViewModel extends ViewModel {

    public ObservableArrayList<SongModel> songs;

    public SongListViewModel() {
        songs = new ObservableArrayList<>();
    }

    public void addSong()
    {
//        songs.add(new SongModel("Red Hot chilly peppers", "snow"));
        songs.set(songs.size()-1, new SongModel("Red Hot chilly peppers", "snow" + new Date().toString()));
    }
}
