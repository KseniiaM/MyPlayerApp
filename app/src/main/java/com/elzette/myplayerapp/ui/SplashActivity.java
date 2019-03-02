package com.elzette.myplayerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    @Inject
    MusicFileSystemScanner mScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((App) getApplication()).playerComponent.injectPlayerProviderComponent(this);
            mScanner.initSongStorage();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //call AFTER all needed work is done
        Intent myIntent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(myIntent);
        finish();
    }
}
