package com.elzette.myplayerapp.providers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.elzette.myplayerapp.services.PlayerService;

//public class NotificationButtonBroadcastReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context ctx, Intent intent) {
//        switch (intent.getStringExtra("extra")) {
//            case "prev":
//                createAndSendIntent(ctx, "prev");
//                break;
//            case "play":
//                createAndSendIntent(ctx, "play");
//                break;
//            case "next":
//                createAndSendIntent(ctx, "next");
//                break;
//        }
//    }
//
//    private void createAndSendIntent(Context context, String action) {
//        Intent intent = new Intent(context, PlayerService.NotificationBroadcastReceiver.class);
//        intent.setAction(action);
//        context.sendBroadcast(intent);
//    }
//}
