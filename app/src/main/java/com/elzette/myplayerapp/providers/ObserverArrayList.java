package com.elzette.myplayerapp.providers;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.ArrayList;
import java.util.List;

public class ObserverArrayList<T> extends ObservableList.OnListChangedCallback<ObservableArrayList<T>> {// extends ArrayList<T> {

    private ObservableArrayList<T> collection;
    private List<UpdateCollectionCallback> collectionUpdateSubscribers;

    public ObserverArrayList() {
        collection = new ObservableArrayList<>();
        collection.addOnListChangedCallback(this);
    }

    @Override
    public void onChanged(ObservableArrayList<T> sender) {
        notifyAllOnUpdated();
    }

    @Override
    public void onItemRangeChanged(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        notifyAllOnUpdated();
    }

    @Override
    public void onItemRangeInserted(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        notifyAllOnUpdated();
    }

    @Override
    public void onItemRangeMoved(ObservableArrayList<T> sender, int fromPosition, int toPosition, int itemCount) {
        notifyAllOnUpdated();
    }

    @Override
    public void onItemRangeRemoved(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        notifyAllOnUpdated();
    }

    public ObserverArrayList(List<T> data) {
        collection = new ObservableArrayList<>();
        collection.addAll(data);
        collection.addOnListChangedCallback(this);
    }

    public ObservableArrayList<T> getValue() {
        if(collection == null) {
            collection = new ObservableArrayList<>();
        }
        return collection;
    }

    private void notifyAllOnUpdated() {
        if(collectionUpdateSubscribers != null)
        collectionUpdateSubscribers.forEach(updateSongsCallback -> updateSongsCallback.onCollectionUpdated(collection));
    }

    public void setValue(List<T> data) {
        collection = new ObservableArrayList<>();
        collection.addAll(data);
        notifyAllOnUpdated();
    }

    public void setCollectionUpdateSubscribers(UpdateCollectionCallback callback) {
        if(collectionUpdateSubscribers == null){
            collectionUpdateSubscribers = new ArrayList<>();
        }
        collectionUpdateSubscribers.add(callback);
    }

    public void removeFromCollectionUpdateSubscribers(UpdateCollectionCallback callback) {
        if(collectionUpdateSubscribers != null) {
            collectionUpdateSubscribers.remove(callback);
        }
    }

    private boolean isCollectionEmpty() {
        return collection == null || collection.isEmpty();
    }
}
