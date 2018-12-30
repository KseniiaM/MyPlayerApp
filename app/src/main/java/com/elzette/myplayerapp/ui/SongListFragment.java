package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.databinding.SongListFragmentBinding;
import com.elzette.myplayerapp.musicRecyclerView.MusicAdapter;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

import java.util.List;

public class SongListFragment extends Fragment {

    private static final String TAG = SongListFragment.class.getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 111;

    private RecyclerView mRecyclerView;
    private MusicAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SongListViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SongListFragmentBinding binding = InitViewModel(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if(PermissionManager.requestReadExternalStoragePermission(this.getActivity())) {
            List<Song> songs = viewModel.getSongsLiveData().getValue();
        //}
        initRecyclerView(this.getView());
        initSongsObserver();
    }

    private SongListFragmentBinding InitViewModel(LayoutInflater inflater) {
        viewModel = ViewModelProviders.of(this)
                                      .get(SongListViewModel.class);
        SongListFragmentBinding binding = SongListFragmentBinding.inflate(inflater);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding;
    }

    private void initSongsObserver() {
        viewModel.getSongsLiveData().observe(this, songs -> {
            Log.d(TAG, "songs " + songs);
            mAdapter.setData(songs);
        });
    }

    private void initRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.song_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MusicAdapter(R.layout.song_item);
        mRecyclerView.setAdapter(mAdapter);
    }
}
