package com.elzette.myplayerapp.models;

import java.io.Serializable;

public class ArtistModel implements MusicItemBaseModel, Serializable {

    private String artist;
    private long songsCount;

    public ArtistModel(String artist, long count) {
        this.artist = artist;
        this.songsCount = count;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public long getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int count) {
        songsCount = count;
    }
}
