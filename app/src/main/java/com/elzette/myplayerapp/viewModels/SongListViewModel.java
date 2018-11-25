package com.elzette.myplayerapp.viewModels;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.elzette.myplayerapp.dal.Song;

public class SongListViewModel extends ViewModel {

    public ObservableArrayList<Song> songs;

    public ObservableField<String> sometext;

    public SongListViewModel() {
        songs = new ObservableArrayList<>();
        sometext = new ObservableField<>("some text");
    }

    public void addSong()
    {
        sometext.set("some new text");
//        songs.add(new Song("Red Hot chilly peppers", "snow"));
//        songs.set(songs.size()-1, new Song("Red Hot chilly peppers", "snow"));
    }
}
