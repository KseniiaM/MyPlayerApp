package com.elzette.myplayerapp.ui.musicRecyclerView;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.databinding.ArtistItemBinding;
import com.elzette.myplayerapp.models.MusicItemBaseModel;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<BaseMusicViewHolder> {

    public RecyclerItemsClickListener onClickListener;
    private List<? extends MusicItemBaseModel> items = new ArrayList<>();
    private int mViewHolderType;

    public void setData(List<? extends MusicItemBaseModel> data) {
        items = data;
        notifyDataSetChanged();
    }

    public MusicAdapter(int viewHolderType)
    {
        mViewHolderType = viewHolderType;
    }

    @NonNull
    @Override
    public BaseMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewDataBinding binding;
        switch (mViewHolderType) {
            case 1:
                binding = DataBindingUtil.inflate(inflater, R.layout.song_item, parent, false);
                return new BaseMusicViewHolder(binding, onClickListener, mViewHolderType);
            case 2:
                binding = DataBindingUtil.inflate(inflater, R.layout.album_item, parent, false);
                return new AlbumViewHolder(binding, onClickListener, mViewHolderType);
            case 3:
                binding = DataBindingUtil.inflate(inflater, R.layout.artist_item, parent, false);
                return new ArtistViewHolder(binding, onClickListener, mViewHolderType);
            default:
                binding = DataBindingUtil.inflate(inflater, R.layout.song_item, parent, false);
                return new BaseMusicViewHolder(binding, onClickListener, mViewHolderType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMusicViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        switch (holder.getItemViewType()) {
            case 1:
                holder.bind(obj);
                break;
            case 2:
                AlbumViewHolder albumVh = (AlbumViewHolder) holder;
                albumVh.bind(obj);
                break;
            case 3:
                ArtistViewHolder artistVh = (ArtistViewHolder) holder;
                artistVh.bind(obj);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mViewHolderType;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected Object getObjForPosition(int position){
        return items.get(position);
    }
}