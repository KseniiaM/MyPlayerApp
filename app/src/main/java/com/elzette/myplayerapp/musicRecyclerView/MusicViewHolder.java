package com.elzette.myplayerapp.musicRecyclerView;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.android.databinding.library.baseAdapters.BR;
import com.elzette.myplayerapp.databinding.SongItemBinding;

public class MusicViewHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;

    public MusicViewHolder(SongItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.SongModel, obj);
        binding.executePendingBindings();
    }

}