package com.elzette.myplayerapp.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {

    private final static String TAG = PermissionManager.class.getSimpleName();
    public final static int REQUEST_READ_STORAGE_PERMISSION = 111;

    private static boolean mIsReadStoragePermissionGranted;

    public static boolean requestReadExternalStoragePermission(Activity activity) {

        int permissionGranted = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                                                                    Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                                              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                              REQUEST_READ_STORAGE_PERMISSION);
            mIsReadStoragePermissionGranted = false;
        }
        else {
            mIsReadStoragePermissionGranted = true;
        }

        return mIsReadStoragePermissionGranted;
    }

    public static boolean checkIfReadStoragePermissionIsGranted() {
        return mIsReadStoragePermissionGranted;
    }

    public static void setReadStoragePermission(boolean permissionResult) { mIsReadStoragePermissionGranted = permissionResult; }
}
