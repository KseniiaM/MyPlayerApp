package com.elzette.myplayerapp.providers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableArrayList<T> extends ArrayList<T> {

    //TODO unsubscribe!
    private List<T> collection;
    private List<UpdateCollectionCallback> collectionUpdateSubscribers;
    private List<IsMusicEmptyCallback> collectonEmptySubscribers;

    public ObservableArrayList() {

    }

    public ObservableArrayList(List<T> data) {
        collection = data;
    }

    private void notifyAllOnUpdated() {
        if(collectionUpdateSubscribers != null)
        collectionUpdateSubscribers.forEach(updateSongsCallback -> updateSongsCallback.onCollectionUpdated(collection));
        if(collectonEmptySubscribers != null)
        collectonEmptySubscribers.forEach(callback -> callback.isMusicCollectionEmptyChanged(isCollectionEmpty()));
    }

    public void setCollection(List<T> data) {
        collection = data;
        notifyAllOnUpdated();
    }

    @Override
    public boolean add(T t) {
        notifyAllOnUpdated();
        return super.add(t);
    }

    @Override
    public void add(int index, T element) {
        notifyAllOnUpdated();
        super.add(index, element);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        notifyAllOnUpdated();
        return super.addAll(c);
    }

    public void setCollectionUpdateSubscribers(UpdateCollectionCallback callback) {
        if(collectionUpdateSubscribers == null){
            collectionUpdateSubscribers = new ArrayList<>();
        }
        collectionUpdateSubscribers.add(callback);
    }

    public void setCollectionEmptySubscribers(IsMusicEmptyCallback callback) {
        if(collectonEmptySubscribers == null){
            collectonEmptySubscribers = new ArrayList<>();
        }
        collectonEmptySubscribers.add(callback);
    }

    private boolean isCollectionEmpty() {
        return collection == null || collection.isEmpty();
    }
}
