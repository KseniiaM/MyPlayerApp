package com.elzette.myplayerapp.models;

public class SongModel {

    private String mArtist;
    private String mTitle;

    public SongModel(String artist, String title) {
        mArtist = artist;
        mTitle = title;
    }

    public String getArtist(){
        return mArtist;
    }

    public void setArtist(String artist){
        mArtist = artist;
    }

    public String getTitle(){
        return mTitle;
    }
}
