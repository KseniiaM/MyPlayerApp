package com.elzette.myplayerapp.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {

    private final static String TAG = PermissionManager.class.getSimpleName();
    private final static int MY_PERMISSIONS_REQUEST_READ_STORAGE = 111;

    private static boolean isPermissionGranted;


    public static void requestReadExternalStoragePermission(Activity activity) {

        int permissionGranted = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                                                                    Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                                              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                              MY_PERMISSIONS_REQUEST_READ_STORAGE);
            isPermissionGranted = false;
        }
        else {
            isPermissionGranted = true;
        }
    }

    public static boolean checkIfPermissionIsGranted() {
        return isPermissionGranted;
    }
}
