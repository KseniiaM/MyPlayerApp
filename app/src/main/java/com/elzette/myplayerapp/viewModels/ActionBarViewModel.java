package com.elzette.myplayerapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.text.format.DateUtils;

import com.elzette.myplayerapp.App;
import com.elzette.myplayerapp.Helpers.SeekBarConverterUtil;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.callbacks.SongChangedCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
import com.elzette.myplayerapp.providers.PlayerConnectionManager;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;

import java.util.List;

import javax.inject.Inject;

public class ActionBarViewModel extends AndroidViewModel implements
                                        UpdateCollectionCallback,
                                        IsMusicPlayingCallback,
                                        SongChangedCallback {

    private static final String ZERO_DURATION = "00:00";
    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public boolean canPlayMusic = false;
    public ObservableInt songProgress = new ObservableInt(0);
    public ObservableField<String> currentPosition = new ObservableField<>(ZERO_DURATION);
    public ObservableField<String> songDuration = new ObservableField<>(ZERO_DURATION);

    private final Handler mHandler = new Handler();
    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (isPlaying.get()) {
                songProgress.set(playerConnectionManager.getSongDurationPercentage());
                //TODO this one is to put to the textview with current duration
                currentPosition.set(playerConnectionManager.getSongDurationString());
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            }
        }
    };

    @Inject
    PlayerConnectionManager playerConnectionManager;

    @Inject
    MusicFileSystemScanner scanner;

    public ActionBarViewModel(Application app) {
        super(app);
        //TODO check if this is needed
        ((App)app).playerComponent.injectPlayerProviderComponent(this);
        scanner.setUpdateCollectionCallback(this);
        playerConnectionManager.setIsMusicPlayingCallback(this);
        playerConnectionManager.setSongChangedCallback(this);
    }

    public void onPlayClick() {
        playerConnectionManager.playMedia();
    }

    public void onPauseClick() {
        playerConnectionManager.pauseMedia();
    }

    public void onNextClick() {
        playerConnectionManager.playNextSong();
    }

    public void onPrevClick() {
        playerConnectionManager.playPrevSong();
    }

    public void changeSongProgress() {playerConnectionManager.changeSongProgress(songProgress.get()); }

    @Override
    public void onCollectionUpdated(List collection) {
        canPlayMusic = !(collection == null || collection.isEmpty());
    }

    @Override
    public void changeMusicPlaybackState(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
        setSongDurationUpdateState(isPlaying);
    }

    @Override
    protected void onCleared() {
        scanner.removeUpdateCollectionCallback(this);
        playerConnectionManager.removeIsMusicPlayingCallback(this);
        playerConnectionManager.removeSongChangedCallback(this);
        super.onCleared();
    }

    @Override
    public void receiveNewCurrentSong(Song song) {
        int duration = song.getDuration();
        songDuration.set(SeekBarConverterUtil.createTimeString(duration));
    }


    private void setSongDurationUpdateState(boolean isSongDurationUpdateNeeded) {
        if (isSongDurationUpdateNeeded) {
            mHandler.post(mUpdateTimeTask);
        } else {
            mHandler.removeCallbacks(mUpdateTimeTask);
        }
    }
}
