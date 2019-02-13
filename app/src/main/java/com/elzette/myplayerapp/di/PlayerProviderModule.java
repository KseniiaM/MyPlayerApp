package com.elzette.myplayerapp.di;

import android.content.Context;

import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.dal.SongDatabase;

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
    public MusicFileSystemScanner getFileSystemScanner(Context context) {
        return new MusicFileSystemScanner(context);
    }
}
