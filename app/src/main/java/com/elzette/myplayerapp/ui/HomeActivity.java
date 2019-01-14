package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
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
}
