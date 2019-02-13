package com.elzette.myplayerapp.providers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.dal.Song;

import java.lang.ref.WeakReference;

import static android.app.Notification.VISIBILITY_PUBLIC;

//TODO create a button to close app notification and service and unregister from broadcast here
public class NotificationProvider {

    public static final int NOTIFICATION_ID = 99;
    public static final String NOTIFICATION_INTENT_FILTER = "NOTIFICATION_INTENT_FILTER";
    String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
    String CHANNEL_NAME = "My Background Service";

    private PendingIntent pIntentPrev;
    private PendingIntent pIntentPlay;
    private PendingIntent pIntentPause;
    private PendingIntent pIntentNext;
    private PendingIntent pIntentClose;

    private WeakReference<Context> context;

    public Notification createNotification(Context context, Song song) {
        Log.d("Notification", "createNotification");
        this.context = new WeakReference<>(context);
        createIntents();
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                                           CHANNEL_NAME,
                                                           NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);
        return buildNotification(song, true);
    }

    public void updateNotification(Song song, boolean isPlayingState) {
        NotificationManager manager =
                (NotificationManager) context.get().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = buildNotification(song, isPlayingState);
        manager.notify(NOTIFICATION_ID, notification);
    }

    public void dismissNotification() {
        NotificationManager manager =
                (NotificationManager) context.get().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }

    private Notification buildNotification(Song song, boolean isPlayingState) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context.get(), NOTIFICATION_CHANNEL_ID);
        RemoteViews content = createRemoteView(song, isPlayingState);
        return notificationBuilder
                .setOngoing(true)
                .setSmallIcon(R.drawable.treble_key)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setCustomContentView(content)
                .setVisibility(VISIBILITY_PUBLIC)
                .build();
    }

    private void createIntents() {
        Intent prevIntent = createIntent(NOTIFICATION_INTENT_FILTER, "prev");
        Intent playIntent = createIntent(NOTIFICATION_INTENT_FILTER, "play");
        Intent pauseIntent = createIntent(NOTIFICATION_INTENT_FILTER, "pause");
        Intent nextIntent = createIntent(NOTIFICATION_INTENT_FILTER, "next");
        Intent closeIntent = createIntent(NOTIFICATION_INTENT_FILTER, "close");
        pIntentPrev = PendingIntent.getBroadcast(context.get(), 1, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pIntentPlay = PendingIntent.getBroadcast(context.get(), 2, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pIntentPause = PendingIntent.getBroadcast(context.get(), 3, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pIntentNext = PendingIntent.getBroadcast(context.get(), 4, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pIntentClose = PendingIntent.getBroadcast(context.get(), 5, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent createIntent(String action, String extra) {
        Log.d("Notification", "Context:" + context.get());
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }

    private RemoteViews createRemoteView(Song song, boolean isPlayingState) {

        RemoteViews remoteViews = new RemoteViews(context.get().getPackageName(), R.layout.notification);

        remoteViews.setTextViewText(R.id.notification_artist, song.getArtist());
        remoteViews.setTextViewText(R.id.notification_title, song.getTitle());

        int isPlayingIconId = isPlayingState ? R.drawable.pause : R.drawable.play;
        remoteViews.setImageViewResource(R.id.notification_play, isPlayingIconId);

        PendingIntent playButtonIntent = isPlayingState ? pIntentPause : pIntentPlay;
        remoteViews.setOnClickPendingIntent(R.id.notification_prev, pIntentPrev);
        remoteViews.setOnClickPendingIntent(R.id.notification_play, playButtonIntent);
        remoteViews.setOnClickPendingIntent(R.id.notification_next, pIntentNext);
        remoteViews.setOnClickPendingIntent(R.id.notification_close, pIntentClose);

        return remoteViews;
    }
}
