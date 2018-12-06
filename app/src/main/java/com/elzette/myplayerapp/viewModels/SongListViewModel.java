package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongListViewModel extends AndroidViewModel {

    private static final String TAG = SongListViewModel.class.getSimpleName();
    private static final String DATABASE_NAME = "song_database";
    //public ObservableArrayList<Song> songs;

    public ObservableField<String> sometext;

    public LiveData<List<Song>> songsLiveData;

    private SongDatabase mSongDatabase;

    //@Inject
    //SongDatabase database;

    public SongListViewModel(Application app) {
        super(app);
        Log.d(TAG, "begin constructor");
        sometext = new ObservableField<>("some text");

        mSongDatabase = Room.databaseBuilder(app.getApplicationContext(), SongDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();

        getAllTracks();
        songsLiveData = mSongDatabase.songDao().getAll();
        }

    public void getAllTracks() {
        // gets all tracks
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getApplication().getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = cr.query(uri, null, selection, null, null );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to db
                mSongDatabase.songDao().insert(new Song(data, title, album, artist));
            }
        }
        cursor.close();
    }

//        mModel = new SongListModel();
//
//        mSongDatabase = mModel.getDatabaseInstance();
//          mSongDatabase = Da
//        SongDao dao = database.songDao();
//        database.songDao().insert(new Song("hi", "hello", "papagena", "aha"));
//        //songsLiveData = database.songDao().getAll();



    public void addSong() {
        sometext.set("some new text");
//        songs.add(new Song("Red Hot chilly peppers", "snow"));
//        songs.set(songs.size()-1, new Song("Red Hot chilly peppers", "snow"));
    }
}
