package com.elzette.myplayerapp;

import android.app.Application;
import android.util.Log;

import com.elzette.myplayerapp.dal.SongDatabase;

public class App extends Application {

    private final static String TAG = App.class.getSimpleName();

    private SongDatabase mDatabase;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
//        mDatabase = DatabaseProvider.getInstance(getApplicationContext()).getDatabase();
//        DatabaseComponent component = DaggerDatabaseComponent.builder()
//                                                             .contextModule(new ContextModule(this))
//                                                             .build();
//
//        SongDatabase s1 = component.getSongDatabase();
//        SongDatabase s2 = component.getSongDatabase();
//
//        Log.e("db", s1 + "here is s1");
//        Log.e("db", s2 + "here is s2");
    }

    public SongDatabase getDatabase() {
        return mDatabase;
    }
}
