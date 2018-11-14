package com.elzette.myplayerapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elzette.myplayerapp.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeActivity extends AppCompatActivity {

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mNavController.navigate(R.id.songListFragment);
        //later add services as observers
        //getLifecycle().addObserver(myServer);
        //startService(new Intent(this, PlayerService.class));
    }

    public void getSongList() {
        //retrieve song info
    }
}
