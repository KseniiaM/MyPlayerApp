package com.elzette.myplayerapp.dal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM Song")
    List<Song> getAll();

    @Query("SELECT * FROM Song WHERE id = :id")
    Song getById(long id);

    @Insert
    void insert(Song song);

    @Update
    void update(Song song);

    @Delete
    void delete(Song song);

}
