package com.elzette.myplayerapp.ui.musicRecyclerView;

import android.databinding.ViewDataBinding;

import com.android.databinding.library.baseAdapters.BR;

public class ArtistViewHolder extends BaseMusicViewHolder {
    public ArtistViewHolder(ViewDataBinding binding, RecyclerItemsClickListener listener, int pageType) {
        super(binding, listener, pageType);
    }

    public void bind(Object obj) {
        binding.setVariable(BR.Artist, obj);
        binding.executePendingBindings();
    }
}
