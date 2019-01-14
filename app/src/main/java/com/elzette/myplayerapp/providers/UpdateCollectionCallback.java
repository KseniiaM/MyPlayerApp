package com.elzette.myplayerapp.providers;

import com.elzette.myplayerapp.dal.Song;

import java.util.List;

public interface UpdateCollectionCallback<T> {
    void onCollectionUpdated(List<T> collection);
}
