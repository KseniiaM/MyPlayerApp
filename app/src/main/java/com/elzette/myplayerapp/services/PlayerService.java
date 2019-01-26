package com.elzette.myplayerapp.services;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.providers.MediaPlayerProvider;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.NotificationProvider;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlayerService extends Service implements UpdateCollectionCallback {

    //TODO create a separate notification manager
    public static final String AUDIO_FILE_DATA = "audio_file_data";

    private List<Song> songs = new ArrayList<>();
    private MediaPlayerProvider mediaPlayerProvider;
    private String mediaFilePath;
    private int currentSongIndex;

    private BroadcastReceiver playNewAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mediaFilePath = intent.getExtras().getString(PlayerConnectionManager.SONG_DATA);
            mediaPlayerProvider.startPlayer(mediaFilePath);
        }
    };

    public BroadcastReceiver notificationButtonBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            switch (intent.getStringExtra("extra")) {
                case "prev":
                    playPrevSong();
                    break;
                case "play":
                    playMedia();
                    break;
                case "next":
                    playNextSong();
                    break;
            }
        }
    };

    private final IBinder iBinder = new LocalBinder();

    @Inject
    MusicFileSystemScanner scanner;

    //TODO will start observing the song list here
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //An audio file is passed to the service through putExtra();
            //mediaFilePath = intent.getExtras().getString(AUDIO_FILE_DATA);
            currentSongIndex = intent.getExtras().getInt(AUDIO_FILE_DATA);
        } catch (NullPointerException e) {
            currentSongIndex = 0;
        }

        ((App) getApplication()).playerComponent.injectPlayerProviderComponent(this);
        scanner.setUpdateCollectionCallback(this);
        subscribeToNotificationButtonBroadcast();

        songs = scanner.getSongs();
        mediaPlayerProvider = new MediaPlayerProvider();
        IntentFilter filter = new IntentFilter(PlayerConnectionManager.PLAY_NEW_SONG);
        registerReceiver(playNewAudioReceiver, filter);

        //if (mediaFilePath != null && !mediaFilePath.equals("")) {
        if (songs != null && songs.size() > currentSongIndex) {
            mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
        }

        runAsForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void runAsForeground() {
        NotificationProvider notificationProvider = new NotificationProvider();
        Notification notification = notificationProvider.createNotification(this, new Song("batat", "batatovich", "", "ivy"));
        startForeground(NotificationProvider.NOTIFICATION_ID, notification);
    }

    public void playMedia() {
        mediaPlayerProvider.playMedia();
    }

    public void pauseMedia() {
        mediaPlayerProvider.pauseMedia();
    }

    public void playNextSong() {
        currentSongIndex = currentSongIndex < (songs.size() - 1) ? currentSongIndex + 1 : 0;
        mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
    }

    public void playPrevSong() {
        currentSongIndex = currentSongIndex == 0 ? songs.size() - 1 : currentSongIndex - 1;
        mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
    }

    public void playSelectedSong(int index) {
        if (index > 0 && index < songs.size()) {
            currentSongIndex = index;
            mediaPlayerProvider.startPlayer(getMediaFilePathFromSongCollection());
        }
    }

    private String getMediaFilePathFromSongCollection() {
        Song currentSong = songs.get(currentSongIndex);
        return currentSong.getData();
    }

//    public void setIsMusicPlayingCallback(IsMusicPlayingCallback isMusicPlayingCallback) {
//        this.isMusicPlayingCallbacks.add(isMusicPlayingCallback);
//    }
//
//    private void notifyAllSubscribersOnMusicStateChange(boolean isPlayingState) {
//        for (IsMusicPlayingCallback callback: isMusicPlayingCallbacks) {
//            callback.changeMusicPlaybackState(isPlayingState);
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
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
        mediaPlayerProvider.destroyMediaPlayer();
        unsubscribeFromNotificationButtons();
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

//    public class NotificationButtonBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context ctx, Intent intent) {
//            switch (intent.getStringExtra("extra")) {
//                case "prev":
//                    playPrevSong();
//                    break;
//                case "play":
//                    playMedia();
//                    break;
//                case "next":
//                    playNextSong();
//                    break;
//            }
//        }
//    }

    private void subscribeToNotificationButtonBroadcast() {
        IntentFilter filter = new IntentFilter(NotificationProvider.NOTIFICATION_INTENT_FILTER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        this.registerReceiver(notificationButtonBroadcastReceiver, filter);
    }

    private void unsubscribeFromNotificationButtons() {
        this.unregisterReceiver(notificationButtonBroadcastReceiver);
    }
}