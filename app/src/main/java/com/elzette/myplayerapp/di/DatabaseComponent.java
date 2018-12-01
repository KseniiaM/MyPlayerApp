package com.elzette.myplayerapp.di;

import com.elzette.myplayerapp.dal.SongDatabase;

import dagger.Component;

@DatabaseApplicationScope
@Component( modules = {DatabaseModule.class})
public interface DatabaseComponent {

    SongDatabase getSongDatabase();
}