package com.elzette.myplayerapp.providers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.elzette.myplayerapp.Helpers.SeekBarConverterUtil;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.callbacks.SongChangedCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.services.PlayerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PlayerConnectionManager implements //IsMusicPlayingCallback,
                                                //SongChangedCallback,
                                                ServiceConnection{

    public static final String PLAY_NEW_SONG = "Play new song";
    public static final String SONG_DATA = "Song data";

    private WeakReference<Context> context;
    private PlayerService playerService;
    boolean serviceBound = false;

    private List<IsMusicPlayingCallback> isMusicPlayingCallbacks = new ArrayList<>();
    private List<SongChangedCallback> songChangedCallbacks = new ArrayList<>();

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
        playerService = binder.getService();
        serviceBound = true;
        notifyIsMusicPlayingCallbacks(true);
//        playerService.setIsMusicPlayingCallback(this);
//        playerService.se
        subscribeToNotificationButtonBroadcast();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        serviceBound = false;
        unsubscribeFromNotificationButtons();
        //playerService.removeMusicPlayingCallback();
    }

    public PlayerConnectionManager(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void playMedia() {
        if(!serviceBound) {
            startPlayerService(0);
            //notifyIsMusicPlayingCallbacks(true);
        }
        else {
            playerService.playMedia();
        }
    }

    public void pauseMedia() {
        if(serviceBound) {
            playerService.pauseMedia();
        }
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

    public void changeSongProgress(int progress) {
        playerService.updateSongProgress(progress);
    }

    public int getSongDurationPercentage() {
        return playerService.getSongDurationPercentage();
    }

    public String getSongDurationString() {
        int duration = playerService.getSongDuration();
        return SeekBarConverterUtil.createTimeString(duration);
    }


    public Song getCurrentSong() {
        if(serviceBound) {
            return playerService.getCurrentSong();
        }
        return null;
    }

    public void setLoopingState(boolean isLooping) {
        playerService.setLoopingState(isLooping);
    }

    public void shullfle() {
        playerService.shuffle();
    }

    public void setIsMusicPlayingCallback(IsMusicPlayingCallback isMusicPlayingCallback) {
        isMusicPlayingCallbacks.add(isMusicPlayingCallback);
    }

    public void removeIsMusicPlayingCallback(IsMusicPlayingCallback callback) {
        isMusicPlayingCallbacks.remove(callback);
    }

    public void notifyIsMusicPlayingCallbacks(boolean state) {
        for (IsMusicPlayingCallback callback: isMusicPlayingCallbacks) {
            callback.changeMusicPlaybackState(state);
        }
    }

    public void setSongChangedCallback(SongChangedCallback callback) {
        songChangedCallbacks.add(callback);
    }

    public void removeSongChangedCallback(SongChangedCallback callback) {
        songChangedCallbacks.remove(callback);
    }

    public void setNewSong(Song song) {
        for (SongChangedCallback callback: songChangedCallbacks) {
            callback.receiveNewCurrentSong(song);
        }
    }

    public void updateSongsCollection(List<Song> songs) {
        playerService.updateSongsSourceCollection(songs);
    }

    private void startPlayerService(int position) {
        Intent playerIntent = new Intent(context.get(), PlayerService.class);
        playerIntent.putExtra(PlayerService.AUDIO_FILE_DATA, position);
        context.get().bindService(playerIntent, this, Context.BIND_AUTO_CREATE);
    }

    public BroadcastReceiver notificationButtonBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            switch (intent.getStringExtra("extra")) {
                case "prev":
                    playerService.playPrevSong();
                    break;
                case "play":
                    playerService.playMedia();
                    break;
                case "pause":
                    playerService.pauseMedia();
                    break;
                case "next":
                    playerService.playNextSong();
                    break;
                case "close":
                    playerService.stopForeground(true);
                    context.get().unbindService(PlayerConnectionManager.this);
                    serviceBound = false;
                    unsubscribeFromNotificationButtons();
                    break;
            }
        }
    };

    private void subscribeToNotificationButtonBroadcast() {
        //Log.d("Notification","subscribeToNotificationButtonBroadcast" + getApplicationContext());
        IntentFilter filter = new IntentFilter(NotificationProvider.NOTIFICATION_INTENT_FILTER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        context.get().registerReceiver(notificationButtonBroadcastReceiver, filter);
    }

    private void unsubscribeFromNotificationButtons() {
        context.get().unregisterReceiver(notificationButtonBroadcastReceiver);
    }
}
