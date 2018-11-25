package com.elzette.myplayerapp.providers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.elzette.myplayerapp.dal.SongDatabase;

public class DatabaseProvider {

    private static final String TAG = DatabaseProvider.class.getSimpleName();

    private static DatabaseProvider instance = null;
    private SongDatabase database;

    private DatabaseProvider(Context context) {
        Log.d(TAG, ">> database initialization");
        database = Room.databaseBuilder(context, SongDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
        Log.d(TAG, "<< database initialization");
    }

    public static synchronized DatabaseProvider getInstance(Context context) {
        Log.d(TAG, "getDatabaseProvider instance");
        if (instance == null) {
            Log.d(TAG, "new DatabaseProvider");
            instance = new DatabaseProvider(context);
        }
        return instance;
    }

    public SongDatabase getDatabase() {
        return database;
    }
}
