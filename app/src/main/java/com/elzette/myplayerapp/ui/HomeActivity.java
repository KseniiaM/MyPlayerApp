package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elzette.myplayerapp.Helpers.PermissionManager;
import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.viewModels.HomeViewModel;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeActivity extends AppCompatActivity {

    private NavController mNavController;
    private HomeViewModel mViewModel;

    private HeadphonesUnpluggedReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mViewModel = ViewModelProviders.of(this)
                                       .get(HomeViewModel.class);

        if(PermissionManager.requestReadExternalStoragePermission(this)) {
            mViewModel.loadMusicData();
        }
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mNavController.navigate(R.id.songListFragment);
    }

    @Override
    protected void onStart() {
        subscribeToHeadphonesState();
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.REQUEST_READ_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.setReadStoragePermission(true);
                    //TODO this one will have some logic of async data loading
                    mViewModel.loadMusicData();
                } else {
                    PermissionManager.setReadStoragePermission(false);
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void subscribeToHeadphonesState() {
        IntentFilter plugReceiver = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiver = new HeadphonesUnpluggedReceiver();
        registerReceiver(receiver, plugReceiver);
    }

    public class HeadphonesUnpluggedReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            switch (intent.getAction())
            {
                case Intent.ACTION_HEADSET_PLUG:
                    handleHeadsetPlugged(intent);
            }
        }

        private void handleHeadsetPlugged(Intent intent) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 1:
                    mViewModel.resumePlayback();
                    break;
                case 0:
                    mViewModel.pausePlayback();
                    break;
            }
        }
    }
}
