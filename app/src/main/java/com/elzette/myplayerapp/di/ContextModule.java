package com.elzette.myplayerapp.di;

import android.content.Context;

import com.elzette.myplayerapp.viewModels.SongListViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context getContext() {
        return context;
    }
}
