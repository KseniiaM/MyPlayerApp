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
import com.elzette.myplayerapp.models.AlbumModel;
import com.elzette.myplayerapp.models.ArtistModel;
import com.elzette.myplayerapp.models.MusicItemBaseModel;
import com.elzette.myplayerapp.ui.musicRecyclerView.MusicAdapter;
import com.elzette.myplayerapp.viewModels.SongListViewModel;

import java.util.List;

public class SongListFragment extends Fragment {

    private static final String TAG = SongListFragment.class.getSimpleName();

    protected RecyclerView mRecyclerView;
    protected MusicAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected SongListViewModel mViewModel;
    protected List<? extends MusicItemBaseModel> currentItems;

    protected int viewHolderType;
    protected int holderLayoutId;
    protected MusicItemBaseModel parentItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewHolderType = MusicAdapter.SONG_VIEW_HOLDER_ID;
        holderLayoutId = R.layout.song_item;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");
        parentItem = (MusicItemBaseModel) getArguments().getSerializable(HomeActivity.SERIALIZED_MODEL);
        SongListFragmentBinding binding = InitViewModel(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView(this.getView());
        initSongsObserver();
    }

    private SongListFragmentBinding InitViewModel(LayoutInflater inflater) {
        mViewModel = ViewModelProviders.of(this)
                                       .get(SongListViewModel.class);
        SongListFragmentBinding binding = SongListFragmentBinding.inflate(inflater);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        return binding;
    }

    private void initSongsObserver() {
        mViewModel.getSongsLiveData().observe(this, songList -> {

            if(songList != null) {
                addCurrentListItems();
            }

            Log.d(TAG, "items" + currentItems.size());
            mAdapter.setData(currentItems);
        });
    }

    protected void initRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.song_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MusicAdapter(viewHolderType, holderLayoutId);
        mAdapter.onClickListener = (v, position) -> {
            choseClickAction(position);
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    protected void addCurrentListItems() {
        if(parentItem == null) {
            currentItems = mViewModel.getSongsLiveData().getValue();
        }
        if(parentItem instanceof AlbumModel) {
            currentItems = mViewModel.getSongsForAlbum((AlbumModel) parentItem);
        }
        if(parentItem instanceof ArtistModel) {
            currentItems = mViewModel.getSongsForArtist((ArtistModel) parentItem);
        }
    }

    protected void choseClickAction(int position) {
        Song selectedSong = (Song) currentItems.get(position);
        mViewModel.choseSongToPlay(selectedSong);
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity != null) {
            activity.navigateToMusicList(R.id.songStatusFragment);
        }
    }
}
