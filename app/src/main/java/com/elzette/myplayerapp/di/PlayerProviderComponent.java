package com.elzette.myplayerapp.di;

import com.elzette.myplayerapp.viewModels.ActionBarViewModel;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

import dagger.Component;

@DatabaseApplicationScope
@Component( modules = {PlayerProviderModule.class})
public interface PlayerProviderComponent {

    void injectPlayerProviderComponent(SongListViewModel vm);
    void injectPlayerProviderComponent(ActionBarViewModel vm);
}
