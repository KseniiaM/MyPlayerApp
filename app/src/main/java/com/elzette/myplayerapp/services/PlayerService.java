package com.elzette.myplayerapp.services;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.providers.MediaPlayerProvider;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.NotificationProvider;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlayerService extends Service implements UpdateCollectionCallback {

    public static final String AUDIO_FILE_DATA = "audio_file_data";

    private List<Song> songs = new ArrayList<>();
    private String mediaFilePath;
    private int currentSongIndex;
    private IsMusicPlayingCallback callback;

    private MediaPlayerProvider mediaPlayerProvider;
    private NotificationProvider notificationProvider;

    private BroadcastReceiver playNewAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mediaFilePath = intent.getExtras().getString(PlayerConnectionManager.SONG_DATA);
            mediaPlayerProvider.startPlayer(mediaFilePath);
        }
    };

//    public BroadcastReceiver notificationButtonBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context ctx, Intent intent) {
//            switch (intent.getStringExtra("extra")) {
//                case "prev":
//                    playPrevSong();
//                    break;
//                case "play":
//                    playMedia();
//                    break;
//                case "pause":
//                    pauseMedia();
//                    break;
//                case "next":
//                    playNextSong();
//                    break;
//                case "close":
//                    stopForeground(true);
//                    ctx.getApplicationContext().unbindService((ServiceConnection) callback);
//                    break;
//            }
//        }
//    };

    private final IBinder iBinder = new LocalBinder();

    @Inject
    MusicFileSystemScanner scanner;

    //TODO will start observing the song list here
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runService(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void runService(Intent intent) {
        try {
            currentSongIndex = intent.getExtras().getInt(AUDIO_FILE_DATA);
        } catch (NullPointerException e) {
            currentSongIndex = 0;
        }

        ((App) getApplication()).playerComponent.injectPlayerProviderComponent(this);
        scanner.setUpdateCollectionCallback(this);
        //subscribeToNotificationButtonBroadcast();

        songs = scanner.getSongs();
        mediaPlayerProvider = new MediaPlayerProvider();
        IntentFilter filter = new IntentFilter(PlayerConnectionManager.PLAY_NEW_SONG);
        registerReceiver(playNewAudioReceiver, filter);

        if (songs != null && songs.size() > currentSongIndex) {
            mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
        }

        runAsForeground();
    }

    private void runAsForeground() {
        notificationProvider = new NotificationProvider();
        Song song = songs.get(currentSongIndex);
        Notification notification = notificationProvider.createNotification(this, song);
        startForeground(NotificationProvider.NOTIFICATION_ID, notification);
    }

    public void playMedia() {
        mediaPlayerProvider.playMedia();
        notifyOnMusicStateChange(true);
    }

    public void pauseMedia() {
        mediaPlayerProvider.pauseMedia();
        notifyOnMusicStateChange(false);
    }

    public void playNextSong() {
        currentSongIndex = currentSongIndex < (songs.size() - 1) ? currentSongIndex + 1 : 0;
        mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
        notifyOnMusicStateChange(true);
    }

    public void playPrevSong() {
        currentSongIndex = currentSongIndex == 0 ? songs.size() - 1 : currentSongIndex - 1;
        mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
        notifyOnMusicStateChange(true);
    }

    public void playSelectedSong(int index) {
        if (index > 0 && index < songs.size()) {
            currentSongIndex = index;
            mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
            notifyOnMusicStateChange(true);
        }
    }

    private String getMediaFilePathFromSongCollection() {
        Song currentSong = songs.get(currentSongIndex);
        return currentSong.getData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        runService(intent);
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        scanner.removeUpdateCollectionCallback(this);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notifyOnMusicStateChange(false);
        notificationProvider.dismissNotification();
        mediaPlayerProvider.destroyMediaPlayer();
        //unsubscribeFromNotificationButtons();
        if (playNewAudioReceiver != null)
            unregisterReceiver(playNewAudioReceiver);
    }

    @Override
    public void onCollectionUpdated(List collection) {
        songs = collection;
    }

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

//    private void subscribeToNotificationButtonBroadcast() {
//        Log.d("Notification","subscribeToNotificationButtonBroadcast" + getApplicationContext());
//        IntentFilter filter = new IntentFilter(NotificationProvider.NOTIFICATION_INTENT_FILTER);
//        filter.addCategory(Intent.CATEGORY_DEFAULT);
//        getApplicationContext().registerReceiver(notificationButtonBroadcastReceiver, filter);
//    }
//
//    private void unsubscribeFromNotificationButtons() {
//        getApplicationContext().unregisterReceiver(notificationButtonBroadcastReceiver);
//    }

    private void notifyOnMusicStateChange(boolean isPlayingState) {
        callback.changeMusicPlaybackState(isPlayingState);
        notificationProvider.updateNotification(songs.get(currentSongIndex), isPlayingState);
    }

    public void setIsMusicPlayingCallback(IsMusicPlayingCallback callback) {
        this.callback = callback;
    }
}