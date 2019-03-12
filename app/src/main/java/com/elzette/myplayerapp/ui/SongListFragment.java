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

    private RecyclerView mRecyclerView;
    private MusicAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SongListViewModel mViewModel;
    private List<? extends MusicItemBaseModel> currentItems;
    private MusicItemBaseModel mParentItem;
    private int mSongDisplayType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");

        mSongDisplayType = getArguments().getInt(HomeActivity.LAYOUT_POSITION);
        mParentItem = (MusicItemBaseModel) getArguments().getSerializable(HomeActivity.SERIALIZED_MODEL);
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

    private void initRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.song_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MusicAdapter(mSongDisplayType);
        mAdapter.onClickListener = (v, position, pageType) -> {
            choseClickAction(position, pageType);
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    private void addCurrentListItems() {
        if(mParentItem == null) {
            currentItems = mViewModel.getFormattedSongList(mSongDisplayType);
        }
        if(mParentItem instanceof AlbumModel) {
            currentItems = mViewModel.getSongsForAlbum((AlbumModel) mParentItem);
        }
        if(mParentItem instanceof ArtistModel) {
            currentItems = mViewModel.getSongsForArtist((ArtistModel) mParentItem);
        }
    }

    private void choseClickAction(int position, int pageType) {
        switch (pageType) {
            case 1:
                Song selectedSong = (Song) currentItems.get(position);
                mViewModel.choseSongToPlay(selectedSong);
                break;
            case 2:
                if(currentItems.get(position) instanceof AlbumModel) {
                    AlbumModel selectedAlbum = (AlbumModel) currentItems.get(position);
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.navigateToSongList(selectedAlbum);
                    //List<Song> songsOfAlbum = mViewModel.getSongsForAlbum(selectedAlbum);

                    //createNewListFragment(songsOfAlbum);
                    //mAdapter.setData(mViewModel.getSongsForAlbum(selectedAlbum));
                }
                break;
            case 3:
                //recreateAdapter(R.layout.song_item);
                currentItems = mViewModel.getFormattedSongList(pageType);
                if(currentItems.get(position) instanceof ArtistModel) {
                    ArtistModel selectedArtist = (ArtistModel) currentItems.get(position);
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.navigateToSongList(selectedArtist);
                    //List<Song> songsOfArtist = mViewModel.getSongsForArtist(selectedArtist);
                    //createNewListFragment(songsOfArtist);
                }
                break;
        }
    }

//    private void createNewListFragment(List<Song> songs) {
//        Log.d(TAG, "createNewListFragment");
//
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
//
//        Bundle bundle = new Bundle();
//        bundle.putInt(PagerAdapter.LAYOUT_ID, R.layout.song_item);
//        bundle.putInt(PagerAdapter.LAYOUT_POSITION, 1);
//
//        SongListFragment fragment = new SongListFragment();
//        fragment.setArguments(bundle);
//        fragment.currentItems = songs;
//        fragTransaction.replace(R.id.song_list_fragment, fragment);
////        fragTransaction.addToBackStack("B");
//        fragTransaction.commitNow();
//    }

//    private void recreateAdapter(int layoutId) {
//        mAdapter = new MusicAdapter(layoutId, mSongDisplayType);
//        mAdapter.onClickListener = (v, position) -> {
//            choseClickAction(position);
//        };
//        mRecyclerView.setAdapter(mAdapter);
//    }
}
