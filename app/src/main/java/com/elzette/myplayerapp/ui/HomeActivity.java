package com.elzette.myplayerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.elzette.myplayerapp.Helpers.PermissionManager;
import com.elzette.myplayerapp.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeActivity extends AppCompatActivity {

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        PermissionManager.requestReadExternalStoragePermission(this);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mNavController.navigate(R.id.songListFragment);
    }
}
