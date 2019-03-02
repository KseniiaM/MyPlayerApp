package com.elzette.myplayerapp.providers;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;

import java.util.List;

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static final String SONGS = "songs";
    private static final String IS_DB_EMPTY = "is db empty";

    private SongDatabase mDatabase;

    public DatabaseManager(Context context) {
        mDatabase = Room.databaseBuilder(context, SongDatabase.class, "database")
                        .allowMainThreadQueries()
                        .build();
    }

    public boolean isDbEmpty() {
        return mDatabase.songDao().getFirstSong() == null;
    }

    public void addSong(Song song) {
        mDatabase.songDao().insert(song);
    }

    public LiveData<List<Song>> getAllSongs() {
        return mDatabase.songDao().getAll();
    }

    public void saveSongsToDb(List<Song> songs) {
        mDatabase.songDao().insert(songs);
    }
}
