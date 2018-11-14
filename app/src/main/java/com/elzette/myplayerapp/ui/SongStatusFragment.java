package com.elzette.myplayerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzette.myplayerapp.models.SongModel;
import com.elzette.myplayerapp.R;

import java.util.List;

public class SongStatusFragment extends Fragment {

    private List<SongModel> mSongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_status_fragment, container, false);
    }

}
