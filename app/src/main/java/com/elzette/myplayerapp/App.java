package com.elzette.myplayerapp;

import android.app.Application;
import android.util.Log;

import com.elzette.myplayerapp.di.ContextModule;
import com.elzette.myplayerapp.di.DaggerDatabaseComponent;
import com.elzette.myplayerapp.di.DaggerPlayerProviderComponent;
import com.elzette.myplayerapp.di.DatabaseComponent;
import com.elzette.myplayerapp.di.DatabaseModule;
import com.elzette.myplayerapp.di.PlayerProviderComponent;
import com.elzette.myplayerapp.di.PlayerProviderModule;

public class App extends Application {

    private final static String TAG = App.class.getSimpleName();

    //public DatabaseComponent dbComponent;
    public PlayerProviderComponent playerComponent;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        ContextModule contextModule = new ContextModule(this);
//        dbComponent = DaggerDatabaseComponent.builder()
//                        .contextModule(contextModule)
//                        .databaseModule(new DatabaseModule())
//                        .build();

        playerComponent = DaggerPlayerProviderComponent.builder()
                        .contextModule(contextModule)
                        .databaseModule(new DatabaseModule())
                        .playerProviderModule(new PlayerProviderModule())
                        .build();
    }
}
