package com.elzette.myplayerapp.callbacks;

import com.elzette.myplayerapp.dal.Song;

public interface SongChangedCallback {

    void receiveNewCurrentSong(Song song);
}
