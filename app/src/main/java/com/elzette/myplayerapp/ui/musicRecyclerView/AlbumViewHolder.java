package com.elzette.myplayerapp.ui.musicRecyclerView;

import android.databinding.ViewDataBinding;

import com.android.databinding.library.baseAdapters.BR;

public class AlbumViewHolder extends BaseMusicViewHolder {

    public AlbumViewHolder(ViewDataBinding binding, RecyclerItemsClickListener listener, int pageType) {
        super(binding, listener, pageType);
    }

    public void bind(Object obj) {
        binding.setVariable(BR.Album, obj);
        binding.executePendingBindings();
    }
}
