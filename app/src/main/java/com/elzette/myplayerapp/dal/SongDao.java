package com.elzette.myplayerapp.dal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM Song WHERE id = :id")
    Song getById(long id);

    @Query("SELECT * FROM Song")
    LiveData<List<Song>> getAll();

    @Insert
    long insert(Song song);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insert(List<Song> songs);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Song song);

    @Delete
    void delete(Song song);

    @Delete
    int delete(List<Song> songs);
}
