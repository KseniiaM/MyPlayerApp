package com.elzette.myplayerapp.dal;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.elzette.myplayerapp.models.MusicItemBaseModel;

@Entity
public class Song implements MusicItemBaseModel {

    @PrimaryKey(autoGenerate = true)
    public long id;
    private String data;
    private String title;
    private String album;
    private String artist;
    private int duration;

    public Song(String data, String title, String album, String artist, int duration) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    //public void setData(String data) {
//        this.data = data;
//    }

    public String getTitle() {
        return title;
    }

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getAlbum() {
        return album;
    }

//    public void setAlbum(String album) {
//        this.album = album;
//    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() { return duration; }

//    public void setArtist(String artist) {
//        this.artist = artist;
//    }
}