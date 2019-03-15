package com.elzette.myplayerapp.ui.musicRecyclerView;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.android.databinding.library.baseAdapters.BR;
import com.elzette.myplayerapp.databinding.SongItemBinding;

public class BaseMusicViewHolder extends RecyclerView.ViewHolder {

    protected RecyclerItemsClickListener mListener;
    protected final ViewDataBinding binding;

    public BaseMusicViewHolder(ViewDataBinding binding, RecyclerItemsClickListener listener, int pageType) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener((view) -> {
            mListener.onClick(view, getAdapterPosition());
        });
        mListener = listener;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.Song, obj);
        binding.executePendingBindings();
    }
}