package com.elzette.myplayerapp.models;

import android.databinding.ObservableField;

public class SongModel {

//    public ObservableField<String> artist;
//    public ObservableField<String> title;
    private String mArtist;
    private String mTitle;

    public SongModel(String artist, String title) {
//        this.artist.set(artist);
//        this.title.set(title);
        mArtist = artist;
        mTitle = title;
    }

    public String getArtist(){
        return mArtist;
    }

    public String getTitle(){
        return mTitle;
    }
}
