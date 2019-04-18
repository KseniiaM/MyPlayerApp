package com.elzette.myplayerapp.providers;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.elzette.myplayerapp.services.PlayerService;

import java.io.IOException;

public class MediaPlayerProvider implements MediaPlayer.OnCompletionListener,
                                            MediaPlayer.OnPreparedListener,
                                            MediaPlayer.OnErrorListener,
                                            AudioManager.OnAudioFocusChangeListener{

    private MediaPlayer mMediaPlayer;
    //private AudioManager audioManager;
    private PlayerService mService;
    private int resumePosition;

    public MediaPlayerProvider(PlayerService service) {
        mService = service;
    }

    public void startPlayer(String mediaFilePath) {
        initMediaPlayer(mediaFilePath);
    }

    private void initMediaPlayer(String mediaFilePath) {
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnPreparedListener(this);
        }

        mMediaPlayer.reset();
        resumePosition = 0;

        mMediaPlayer.setAudioAttributes(getAttributes());
        try {
            // Set the data source to the mediaFile location
            mMediaPlayer.setDataSource(mediaFilePath);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            e.printStackTrace();
        }
    }

    public void playMedia() {
        if (mMediaPlayer == null) return;

        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(resumePosition);
            mMediaPlayer.start();
        }
    }

    public void stopMedia() {
        if (mMediaPlayer == null) return;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            resumePosition = 0;
        }
    }

    public void pauseMedia() {
        if (mMediaPlayer == null) return;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            resumePosition = mMediaPlayer.getCurrentPosition();
        }
    }

    public void seekToPosition(int millis) {
        mMediaPlayer.seekTo(millis);
    }

    public long getCurrentSongProgress() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void setLoopingState(boolean isLooping) {
        mMediaPlayer.setLooping(isLooping);
    }

    public void destroyMediaPlayer() {
        if (mMediaPlayer != null) {
            stopMedia();
            mMediaPlayer.release();
        }
        //removeAudioFocus();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mService.playNextSong();
    }

    private AudioAttributes getAttributes() {
        return new AudioAttributes.Builder()
                                  .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                  .setUsage(AudioAttributes.USAGE_MEDIA)
                                  .build();
    }
//
//    private boolean requestAudioFocus() {
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            //Focus gained
//            return true;
//        }
//        //Could not gain focus
//        return false;
//    }
//
//    private boolean removeAudioFocus() {
//        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
//                audioManager.abandonAudioFocus(this);
//    }

    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mMediaPlayer == null) {
                    initMediaPlayer("");
                }
                else if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();
                mMediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(0.1f, 0.1f);
                }
                break;
        }
    }
}
