package com.elzette.myplayerapp.di;

import android.content.Context;

import com.elzette.myplayerapp.providers.DatabaseManager;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class, DatabaseModule.class})
public class PlayerProviderModule {

    @Provides
    @DatabaseApplicationScope
    public PlayerConnectionManager getPlayerProvider(Context context) {
        return new PlayerConnectionManager(context);
    }

    @Provides
    @DatabaseApplicationScope
    public MusicFileSystemScanner getFileSystemScanner(Context context, DatabaseManager dbManager) {
        return new MusicFileSystemScanner(context, dbManager);
    }
}
