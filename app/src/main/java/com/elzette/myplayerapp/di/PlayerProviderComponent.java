package com.elzette.myplayerapp.di;

import com.elzette.myplayerapp.services.PlayerService;
import com.elzette.myplayerapp.viewModels.ActionBarViewModel;
import com.elzette.myplayerapp.viewModels.HomeViewModel;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

import dagger.Component;

@DatabaseApplicationScope
@Component( modules = {PlayerProviderModule.class})
public interface PlayerProviderComponent {

    void injectPlayerProviderComponent(SongListViewModel vm);
    void injectPlayerProviderComponent(ActionBarViewModel vm);
    void injectPlayerProviderComponent(HomeViewModel vm);
    void injectPlayerProviderComponent(PlayerService service);
}
