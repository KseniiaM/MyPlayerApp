package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.databinding.SongListFragmentBinding;
import com.elzette.myplayerapp.databinding.SongStatusFragmentBinding;
import com.elzette.myplayerapp.viewModels.SongListViewModel;
import com.elzette.myplayerapp.viewModels.SongStatusViewModel;

import java.util.List;

public class SongStatusFragment extends Fragment {

    protected SongStatusViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SongStatusFragmentBinding binding = InitViewModel(inflater);
        return binding.getRoot();
    }

    private SongStatusFragmentBinding InitViewModel(LayoutInflater inflater) {
        mViewModel = ViewModelProviders.of(this)
                .get(SongStatusViewModel.class);
        SongStatusFragmentBinding binding = SongStatusFragmentBinding.inflate(inflater);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        return binding;
    }
}
