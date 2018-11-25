package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
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

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.databinding.SongListFragmentBinding;
import com.elzette.myplayerapp.musicRecyclerView.MusicAdapter;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

public class SongListFragment extends Fragment {

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

        initRecyclerView(view);
        mButton = view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.songs.get(0).setArtist("ivy");
                mAdapter.notifyDataSetChanged();
                viewModel.addSong();
            }
        });
    }

    private void initRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.song_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //viewModel.songs.add(new Song("Red Hot chilly peppers", "californiacation"));

        mAdapter = new MusicAdapter(viewModel.songs, R.layout.song_item);
        mRecyclerView.setAdapter(mAdapter);
    }
}
