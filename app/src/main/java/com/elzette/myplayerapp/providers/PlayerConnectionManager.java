package com.elzette.myplayerapp.providers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.services.PlayerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PlayerConnectionManager implements IsMusicPlayingCallback, ServiceConnection{

    public static final String PLAY_NEW_SONG = "Play new song";
    public static final String SONG_DATA = "Song data";

    private WeakReference<Context> context;
    private PlayerService playerService;
    boolean serviceBound = false;

    private List<IsMusicPlayingCallback> isMusicPlayingCallbacks = new ArrayList<>();

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
        playerService = binder.getService();
        serviceBound = true;
        playerService.setIsMusicPlayingCallback(this);
    }

    @Override
    public void onBindingDied(ComponentName name) {
        playerService.removeIsMusicPlayingCallback();
        serviceBound = false;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerService.removeIsMusicPlayingCallback();
        serviceBound = false;
    }

    public PlayerConnectionManager(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void playMedia() {
        if(!serviceBound) {
            startPlayerService(0);
            notifyIsMusicPlayingCallbacks(true);
        }
        else {
            playerService.playMedia();
        }
    }

    //should disable these 4 methods if playback not started yet

    public void pauseMedia() {
        playerService.pauseMedia();
    }

    public void playPrevSong() {
        if(serviceBound) {
            playerService.playPrevSong();
        }
    }

    public void playNextSong() {
        if(serviceBound) {
            playerService.playNextSong();
        }
    }

    public void playSelectedSong(int position) {
        if(!serviceBound) {
            startPlayerService(position);
        }
        else {
            playerService.playSelectedSong(position);
        }
    }

    public void setIsMusicPlayingCallback(IsMusicPlayingCallback isMusicPlayingCallback) {
        isMusicPlayingCallbacks.add(isMusicPlayingCallback);
    }

    public void removeIsMusicPlayingCallback(IsMusicPlayingCallback callback) {
        isMusicPlayingCallbacks.remove(callback);
    }

    private void notifyIsMusicPlayingCallbacks(boolean state) {
        for (IsMusicPlayingCallback callback: isMusicPlayingCallbacks) {
            callback.changeMusicPlaybackState(state);
        }
    }

    @Override
    public void changeMusicPlaybackState(boolean isPlaying) {
        notifyIsMusicPlayingCallbacks(isPlaying);
    }

    private void startPlayerService(int position) {
        Intent playerIntent = new Intent(context.get(), PlayerService.class);
        playerIntent.putExtra(PlayerService.AUDIO_FILE_DATA, position);
        //needs to be both started and bound so music will play while the app is not active
        context.get().startService(playerIntent);
        context.get().bindService(playerIntent, this, Context.BIND_AUTO_CREATE);
    }
}
