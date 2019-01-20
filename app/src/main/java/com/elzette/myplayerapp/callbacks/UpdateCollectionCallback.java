package com.elzette.myplayerapp.callbacks;

import com.elzette.myplayerapp.dal.Song;

import java.util.List;

public interface UpdateCollectionCallback<T> {
    void onCollectionUpdated(List<T> collection);
}
