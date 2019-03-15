package com.elzette.myplayerapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.models.AlbumModel;
import com.elzette.myplayerapp.ui.musicRecyclerView.MusicAdapter;

public class AlbumListFragment extends SongListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHolderType = MusicAdapter.ALBUM_VIEW_HOLDER_ID;
        holderLayoutId = R.layout.album_item;
    }

    @Override
    protected void addCurrentListItems() {
        currentItems = mViewModel.getAlbums();
    }

    @Override
    protected void choseClickAction(int position) {
        AlbumModel selectedAlbum = (AlbumModel) currentItems.get(position);
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity != null) {
            activity.navigateToSongList(selectedAlbum);
        }
    }
}
