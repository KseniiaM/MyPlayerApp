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

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.providers.MediaPlayerProvider;
import com.elzette.myplayerapp.providers.NotificationProvider;
import com.elzette.myplayerapp.providers.PlayerManager;

public class PlayerService extends Service {

    //TODO create a separate notification manager
    public static final String AUDIO_FILE_DATA = "audio_file_data";

    private MediaPlayerProvider mediaPlayerProvider;
    private String mediaFilePath;

    private BroadcastReceiver playNewAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mediaFilePath = intent.getExtras().getString(PlayerManager.SONG_DATA);
            mediaPlayerProvider.startPlayer(mediaFilePath);
        }
    };

    public void playMedia() {
        mediaPlayerProvider.playMedia();
    }

    public void pauseMedia() {
        mediaPlayerProvider.pauseMedia();
    }

    private final IBinder iBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //An audio file is passed to the service through putExtra();
            mediaFilePath = intent.getExtras().getString(AUDIO_FILE_DATA);
        } catch (NullPointerException e) {
            stopSelf();
        }

        mediaPlayerProvider = new MediaPlayerProvider();
        IntentFilter filter = new IntentFilter(PlayerManager.PLAY_NEW_SONG);
        registerReceiver(playNewAudioReceiver, filter);

        if (mediaFilePath != null && !mediaFilePath.equals("")) {
            mediaPlayerProvider.startPlayer(mediaFilePath);
        }

        runAsForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void runAsForeground() {
        NotificationProvider notificationProvider = new NotificationProvider();
        Notification notification = notificationProvider.createNotification(this, new Song("batat", "batatovich","","ivy"));
        startForeground(NotificationProvider.NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerProvider.destroyMediaPlayer();

        if(playNewAudioReceiver != null)
            unregisterReceiver(playNewAudioReceiver);
    }


    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }


    public class NotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "prev":
                    Toast t = Toast.makeText(context, "prev", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                case "play":
                    Toast t1 = Toast.makeText(context, "play", Toast.LENGTH_SHORT);
                    t1.show();
                    break;
                case "next":
                    Toast t2 = Toast.makeText(context, "next", Toast.LENGTH_SHORT);
                    t2.show();
                    break;
            }
        }
    }
}