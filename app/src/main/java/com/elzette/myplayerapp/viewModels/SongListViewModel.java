package com.elzette.myplayerapp.viewModels;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.util.Log;

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.di.ContextModule;

import javax.inject.Inject;

public class SongListViewModel extends ViewModel {

    private static final String TAG = SongListViewModel.class.getSimpleName();

    public ObservableArrayList<Song> songs;

    public ObservableField<String> sometext;

    //public LiveData<List<Song>> songsLiveData;

    private SongDatabase mSongDatabase;

//    private SongListModel mModel;
//
    @Inject
    public SongListViewModel(SongDatabase db) {
        Log.d(TAG, "begin constructor");
        songs = new ObservableArrayList<>();
        sometext = new ObservableField<>("some text");
        mSongDatabase = db;
//        mModel = new SongListModel();
//
//        mSongDatabase = mModel.getDatabaseInstance();
//          mSongDatabase = Da
//        SongDao dao = database.songDao();
//        database.songDao().insert(new Song("hi", "hello", "papagena", "aha"));
//        //songsLiveData = database.songDao().getAll();


    }

    public void addSong() {
        sometext.set("some new text");
//        songs.add(new Song("Red Hot chilly peppers", "snow"));
//        songs.set(songs.size()-1, new Song("Red Hot chilly peppers", "snow"));
    }
}
