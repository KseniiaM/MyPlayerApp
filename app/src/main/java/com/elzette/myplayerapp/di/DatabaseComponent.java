package com.elzette.myplayerapp.di;

import android.content.Context;

import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

import dagger.Component;

@DatabaseApplicationScope
@Component( modules = {DatabaseModule.class})
public interface DatabaseComponent {

    //SongDatabase getSongDatabase();
    void injectDatabaseComponent(SongListViewModel vm);

    void injectContextModule(SongListViewModel vm);
}