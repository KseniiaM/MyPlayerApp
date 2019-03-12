package com.elzette.myplayerapp.models;

import java.io.Serializable;

public class AlbumModel implements MusicItemBaseModel, Serializable {

    private String artist;
    private String album;
    private long songsCount;

    public AlbumModel(String artist, String album, long count) {
        this.album = album;
        this.artist = artist;
        this.songsCount = count;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int count) {
        songsCount = count;
    }
}
