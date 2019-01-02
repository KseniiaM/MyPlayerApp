package com.elzette.myplayerapp.musicRecyclerView;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.databinding.SongItemBinding;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {

    public RecyclerItemsClickListener onClickListener;
    private List<Song> items = new ArrayList<>();
    private int mLayoutId;

    public void setData(List<Song> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public MusicAdapter(int layoutId)
    {
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SongItemBinding binding = DataBindingUtil.inflate(inflater, mLayoutId, parent, false);
        return new MusicViewHolder(binding, onClickListener);
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