package com.elzette.myplayerapp.musicRecyclerView;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.elzette.myplayerapp.databinding.SongItemBinding;

public class MusicViewHolder extends RecyclerView.ViewHolder {

    private RecyclerItemsClickListener mListener;
    private final ViewDataBinding binding;

    public MusicViewHolder(SongItemBinding binding, RecyclerItemsClickListener listener) {
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