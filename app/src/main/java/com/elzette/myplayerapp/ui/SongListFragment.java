package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elzette.myplayerapp.Helpers.PermissionManager;
import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.databinding.SongListFragmentBinding;
import com.elzette.myplayerapp.musicRecyclerView.MusicAdapter;
import com.elzette.myplayerapp.services.PlayerService;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

import java.util.List;

public class SongListFragment extends Fragment {

    private static final String TAG = SongListFragment.class.getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 111;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SongListViewModel viewModel;

    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = ViewModelProviders.of(this)
                                      .get(SongListViewModel.class);

        SongListFragmentBinding binding = //DataBindingUtil.inflate(inflater, R.layout.song_list_fragment, container, false);
                SongListFragmentBinding.inflate(inflater);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(PermissionManager.requestReadExternalStoragePermission(this.getActivity())) {
            viewModel.getAllTracks();
            List<Song> songs = viewModel.songsLiveData.getValue();

            if(songs != null)
            ((HomeActivity)getActivity()).playAudio(songs.get(0).getData());
        }
//        viewModel.songs.observe(this, new Observer<List<Song>>() {
//            @Override
//            public void onChanged(@Nullable List<Song> songs) {
//                Log.i(TAG, "onChanged " + songs);
//                mAdapter.notifyDataSetChanged();
//            }
//        });

        initRecyclerView(view);
        mButton = view.findViewById(R.id.button);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewModel.songs.get(0).setArtist("ivy");
//                mAdapter.notifyDataSetChanged();
//                viewModel.addSong();
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getAllTracks();
                }
            }

            // other 'case' statements for other permssions
        }
    }

    private void initRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.song_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //viewModel.songs.add(new Song("Red Hot chilly peppers", "californiacation"));

        mAdapter = new MusicAdapter(viewModel.songsLiveData.getValue(), R.layout.song_item);
        mRecyclerView.setAdapter(mAdapter);
    }
}
