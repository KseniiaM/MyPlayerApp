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

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongListViewModel extends AndroidViewModel {

    private static final String TAG = SongListViewModel.class.getSimpleName();
    private static final String DATABASE_NAME = "song_database";
    public ObservableArrayList<Song> songs;

    public ObservableField<String> sometext;

    private MutableLiveData<List<Song>> songsLiveData;

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

        songs = new ObservableArrayList<>();
        getAllTracks();
        //songsLiveData = mSongDatabase.songDao().getAll();
        }

    public MutableLiveData<List<Song>> getSongsLiveData() {
        if (songsLiveData == null) {
            songsLiveData = new MutableLiveData<List<Song>>();
        }
        return songsLiveData;
    }

    public void getAllTracks() {
        // gets all tracks
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getApplication().getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = cr.query(uri, null, null, null, null );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to db
                Song songToAdd = new Song(data, title, album, artist);
                mSongDatabase.songDao().insert(songToAdd);
                //songsLiveData.getValue().add(songToAdd);
            }
//            songsLiveData = mSongDatabase.songDao().getAll();
            List<Song> songs;
            songs = mSongDatabase.songDao().getAll();
            getSongsLiveData().setValue(songs);
        }
        else {
//            List<Song> songs = new ArrayList<Song>();
//            List<Song> s = songsLiveData.getValue();
//            s.addAll(songs);

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
