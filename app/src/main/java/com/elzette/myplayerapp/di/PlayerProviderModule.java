package com.elzette.myplayerapp.di;

import android.content.Context;

import com.elzette.myplayerapp.Helpers.PlayerProvider;
import com.elzette.myplayerapp.dal.SongDatabase;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class, DatabaseModule.class})
public class PlayerProviderModule {

    @Provides
    @DatabaseApplicationScope
    public PlayerProvider getPlayerProvider(Context context, SongDatabase db) {
        return new PlayerProvider(context, db);
    }
}
