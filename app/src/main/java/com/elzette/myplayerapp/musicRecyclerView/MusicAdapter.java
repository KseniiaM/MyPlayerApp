package com.elzette.myplayerapp.musicRecyclerView;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elzette.myplayerapp.databinding.SongItemBinding;
import com.elzette.myplayerapp.models.SongModel;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {

    private List<SongModel> items;
    private int mLayoutId;

    public void setData(List<SongModel> data) {
        items.addAll(data);
    }

    public MusicAdapter(List<SongModel> songs, int layoutId)
    {
        this.items = songs;
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SongItemBinding binding = DataBindingUtil.inflate(inflater, mLayoutId, parent, false);
        return new MusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected Object getObjForPosition(int position){
        return items.get(position);
    }
}