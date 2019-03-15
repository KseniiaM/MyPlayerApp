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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<BaseMusicViewHolder> {

    public static final int SONG_VIEW_HOLDER_ID = 1;
    public static final int ALBUM_VIEW_HOLDER_ID = 2;
    public static final int ARTIST_VIEW_HOLDER_ID = 3;

    public RecyclerItemsClickListener onClickListener;
    private List<? extends MusicItemBaseModel> items = new ArrayList<>();
    private int mViewHolderType;
    private int mViewHolderLayoutId;

    public void setData(List<? extends MusicItemBaseModel> data) {
        items = data;
        notifyDataSetChanged();
    }

    public MusicAdapter(int viewHolderType, int layoutId)
    {
        mViewHolderType = viewHolderType;
        mViewHolderLayoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, mViewHolderLayoutId, parent, false);
        switch (mViewHolderType) {
            case SONG_VIEW_HOLDER_ID:
                return new BaseMusicViewHolder(binding, onClickListener, mViewHolderType);
            case ALBUM_VIEW_HOLDER_ID:
                return new AlbumViewHolder(binding, onClickListener, mViewHolderType);
            case ARTIST_VIEW_HOLDER_ID:
                return new ArtistViewHolder(binding, onClickListener, mViewHolderType);
            default:
                return new BaseMusicViewHolder(binding, onClickListener, mViewHolderType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMusicViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        switch (holder.getItemViewType()) {
            case SONG_VIEW_HOLDER_ID:
                holder.bind(obj);
                break;
            case ALBUM_VIEW_HOLDER_ID:
                AlbumViewHolder albumVh = (AlbumViewHolder) holder;
                albumVh.bind(obj);
                break;
            case ARTIST_VIEW_HOLDER_ID:
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