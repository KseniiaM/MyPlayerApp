package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.persistence.room.Update;
import android.content.res.Resources;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.callbacks.SongChangedCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;

import javax.inject.Inject;

public class SongStatusViewModel extends AndroidViewModel implements SongChangedCallback {

    public ObservableField<Bitmap> albumCover = new ObservableField<Bitmap>();

    public ObservableField<String> title = new ObservableField<>("");

    public ObservableField<String> artist = new ObservableField<>("");

    @Inject
    PlayerConnectionManager playerConnectionManager;

    public SongStatusViewModel(@NonNull Application application) {
        super(application);
        //TODO check if this is needed
        ((App)application).playerComponent.injectPlayerProviderComponent(this);
        playerConnectionManager.setSongChangedCallback(this);
        displayCurrentSong();
    }

    @Override
    public void receiveNewCurrentSong(Song song) {
        displayCurrentSong();
    }

    @Override
    protected void onCleared() {
        playerConnectionManager.removeSongChangedCallback(this);
        super.onCleared();
    }

    private void displayCurrentSong() {
        Song currentSong = playerConnectionManager.getCurrentSong();
        displayCurrentSong(currentSong);
    }

    private void displayCurrentSong(Song currentSong) {
        if(currentSong != null) {
            title.set(currentSong.getTitle());
            artist.set(currentSong.getArtist());
            new UpdateAlbumCoverThread(currentSong.getData());
        }
    }

//    @Bindable
//    public Drawable getAlbumCover() {
//        return new BitmapDrawable(Resources.getSystem(), albumCover);
//    }

    class UpdateAlbumCoverThread implements Runnable {

        private Thread mThread;
        private String mSongPath;

        UpdateAlbumCoverThread(String songPath) {
            mSongPath = songPath;
            mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mSongPath);

            byte [] data = mmr.getEmbeddedPicture();

            Bitmap bitmap;

            if(data != null)
            {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            else
            {
                bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.treble_key);
            }

            albumCover.set(bitmap);
        }
    }
}
