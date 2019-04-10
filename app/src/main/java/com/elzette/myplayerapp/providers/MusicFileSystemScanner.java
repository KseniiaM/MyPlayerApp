package com.elzette.myplayerapp.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.provider.MediaStore;

import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;
import com.elzette.myplayerapp.dal.Song;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MusicFileSystemScanner {

    WeakReference<Context> context;
    DatabaseManager dbManager;

    private ObserverArrayList<Song> songs = new ObserverArrayList<>();

    public MusicFileSystemScanner(Context context, DatabaseManager dbManager) {
        this.context = new WeakReference<>(context);
        this.dbManager = dbManager;

//         Observable.just(songs.getValue())
//                   .subscribeOn(Schedulers.io())
//                   .observeOn(AndroidSchedulers.mainThread())
//                   .subscribe(value -> dbManager.saveSongsToDb(value));
    }

    public void setUpdateCollectionCallback(UpdateCollectionCallback callback) {
        songs.setCollectionUpdateSubscribers(callback);
    }

    public void removeUpdateCollectionCallback(UpdateCollectionCallback callback) {
        songs.removeFromCollectionUpdateSubscribers(callback);
    }

    public ObservableArrayList<Song> getSongs() {
        return songs.getValue();
    }

    public void initSongStorage() {
        //checks if some of the songs already present, launch synchronous scan if not
        //Disposable isFirstLaunch = Observable.just(dbManager.isDbEmpty()).subscribeOn(Schedulers.computation()).subscribe();
        if (true) {
            //launch sync scan
            loadSongs();
        }
        else {
            //start async scan to compare db and file system and return immediately
        }
    }

    public List<Song> loadSongs() {       //To be moved out (suggest to have the initial scanning activity)
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.get().getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = cr.query(uri, null, null, null, null );

        List<Song> tempSongListInsteadOfDb = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                // Save to db
                Song songToAdd = new Song(data, title, album, artist, duration);
                tempSongListInsteadOfDb.add(songToAdd);
                //dbManager.addSong(songToAdd);
                //songsLiveData.getValue().add(songToAdd);
            }
//            songsLiveData = mSongDatabase.songDao().getAll();
            //songs.getValue().addAll(mSongDatabase.songDao().getAll());
            //songs.getValue().addAll(tempSongListInsteadOfDb);
            songs.setValue(tempSongListInsteadOfDb);
//            songsUpdated(songs);
        }
        else {
//            List<Song> songs = new ArrayList<Song>();
//            List<Song> s = songsLiveData.getValue();
//            s.addAll(songs);

        }
        if (cursor != null) {
            cursor.close();
        }

        return tempSongListInsteadOfDb;
    }
}
