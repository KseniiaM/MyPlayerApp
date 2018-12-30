package com.elzette.myplayerapp.di;

import dagger.Component;

//TODO check if this is necessary and delete
@DatabaseApplicationScope
@Component( modules = {DatabaseModule.class})
public interface DatabaseComponent {

//    void injectDatabaseComponent(HomeViewModel vm);
}