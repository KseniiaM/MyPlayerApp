package com.elzette.myplayerapp;

import android.app.Application;
import android.util.Log;

import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.di.ContextModule;
import com.elzette.myplayerapp.di.DaggerDatabaseComponent;
import com.elzette.myplayerapp.di.DatabaseComponent;
import com.elzette.myplayerapp.di.DatabaseModule;

public class App extends Application {

    private final static String TAG = App.class.getSimpleName();

    //private SongDatabase database;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        //mDatabase = DatabaseProvider.getInstance(getApplicationContext()).getDatabase();
//        DatabaseComponent component = DaggerDatabaseComponent.builder()
//                                                             .contextModule(new ContextModule(this))
//                                                             .databaseModule(new DatabaseModule())
//                                                             .build();
        //database = component.getSongDatabase();
        //Log.d(TAG, "batat: " + database);

    }
}
