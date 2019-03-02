package com.elzette.myplayerapp.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.providers.DatabaseManager;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DatabaseModule {

    @Provides
    @DatabaseApplicationScope
    public DatabaseManager getDatabaseManager(Context context) {
        return new DatabaseManager(context);
    }
}
