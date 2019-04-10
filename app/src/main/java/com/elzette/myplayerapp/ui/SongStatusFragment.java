package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.databinding.SongStatusFragmentBinding;
import com.elzette.myplayerapp.viewModels.SongStatusViewModel;


public class SongStatusFragment extends Fragment {

    protected SongStatusViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SongStatusFragmentBinding binding = InitViewModel(inflater);
        return binding.getRoot();
    }

    private SongStatusFragmentBinding InitViewModel(LayoutInflater inflater) {
        mViewModel = ViewModelProviders.of(this)
                .get(SongStatusViewModel.class);
        SongStatusFragmentBinding binding = SongStatusFragmentBinding.inflate(inflater);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        return binding;
    }

//    @BindingAdapter("android:src")
//    public static void setAlbumCover(ImageView view, Drawable drawable) {
//        view.setImageDrawable(drawable);
//    }

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, Bitmap bitmap) {
        if(bitmap != null) {
            view.setImageBitmap(bitmap);
        }
        else {
            view.setImageResource(R.drawable.treble_key);
        }
    }
}
