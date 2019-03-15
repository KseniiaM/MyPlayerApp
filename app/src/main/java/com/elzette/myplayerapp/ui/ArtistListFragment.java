package com.elzette.myplayerapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.models.ArtistModel;
import com.elzette.myplayerapp.ui.musicRecyclerView.MusicAdapter;

public class ArtistListFragment extends SongListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHolderType = MusicAdapter.ARTIST_VIEW_HOLDER_ID;
        holderLayoutId = R.layout.artist_item;
    }

    @Override
    protected void addCurrentListItems() {
        currentItems = mViewModel.getArtists();
    }

    @Override
    protected void choseClickAction(int position) {
        ArtistModel selectedArtist = (ArtistModel) currentItems.get(position);
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity != null) {
            activity.navigateToSongList(selectedArtist);
        }
    }
}
