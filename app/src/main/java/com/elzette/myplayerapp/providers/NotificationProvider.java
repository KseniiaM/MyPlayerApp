package com.elzette.myplayerapp.providers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.services.PlayerService;

import java.lang.ref.WeakReference;

//TODO create a button to close app notification and service and unregister from broadcast here
public class NotificationProvider {

    public static final int NOTIFICATION_ID = 99;
    public static final String NOTIFICATION_INTENT_FILTER = "NOTIFICATION_INTENT_FILTER";
    String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
    String CHANNEL_NAME = "My Background Service";

    private PendingIntent pIntent1;
    private PendingIntent pIntent2;
    private PendingIntent pIntent3;

//    private NotificationButtonBroadcastReceiver receiver;
    private WeakReference<Context> context;

    public Notification createNotification(Context context, Song song) {
        this.context = new WeakReference<>(context);
        createIntents();
        //subscribeToNotificationButtonBoradcast();
        RemoteViews notificationContent = createRemoteView(song);

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.treble_key)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setCustomContentView(notificationContent)
                .build();

        return notification;
    }

    private void createIntents() {
        Intent prevIntent = createIntent(NOTIFICATION_INTENT_FILTER, "prev");
        Intent playIntent = createIntent(NOTIFICATION_INTENT_FILTER, "play");
        Intent nextIntent = createIntent(NOTIFICATION_INTENT_FILTER, "next");
        pIntent1 = PendingIntent.getBroadcast(context.get(), 1, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pIntent2 = PendingIntent.getBroadcast(context.get(), 2, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pIntent3 = PendingIntent.getBroadcast(context.get(), 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent createIntent(String action, String extra) {
        Intent intent = new Intent(context.get(), BroadcastReceiver.class);
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }

    private RemoteViews createRemoteView(Song song) {
        RemoteViews remoteViews = new RemoteViews(context.get().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.notification_artist, song.getArtist());
        remoteViews.setTextViewText(R.id.notification_title, song.getTitle());
        remoteViews.setOnClickPendingIntent(R.id.notification_prev, pIntent1);
        remoteViews.setOnClickPendingIntent(R.id.notification_play, pIntent2);
        remoteViews.setOnClickPendingIntent(R.id.notification_next, pIntent3);
        return remoteViews;
    }

//    private void subscribeToNotificationButtonBoradcast() {
//        IntentFilter filter = new IntentFilter(NOTIFICATION_INTENT_FILTER);
//        receiver = new PlayerService.NotificationButtonBroadcastReceiver();
//        context.get().registerReceiver(receiver, filter);
//    }
//
//    private void unsubscribeFromNotificationButtons() {
//        context.get().unregisterReceiver(receiver);
//    }
}
