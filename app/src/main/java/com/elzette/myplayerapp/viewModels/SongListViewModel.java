package com.elzette.myplayerapp.viewModels;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.elzette.myplayerapp.models.SongModel;

public class SongListViewModel extends ViewModel {

    public ObservableArrayList<SongModel> songs;

    public ObservableField<String> sometext;

    public SongListViewModel() {
        songs = new ObservableArrayList<>();
        sometext = new ObservableField<>("some text");
    }

    public void addSong()
    {
        sometext.set("some new text");
        songs.add(new SongModel("Red Hot chilly peppers", "snow"));
        songs.set(songs.size()-1, new SongModel("Red Hot chilly peppers", "snow"));
    }
}
