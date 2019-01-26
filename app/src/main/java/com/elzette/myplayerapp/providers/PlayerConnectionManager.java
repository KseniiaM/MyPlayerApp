package com.elzette.myplayerapp.providers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.ObservableArrayList;
import android.os.IBinder;

import com.elzette.myplayerapp.callbacks.BoundToServiceCallback;
import com.elzette.myplayerapp.callbacks.IsMusicPlayingCallback;
import com.elzette.myplayerapp.callbacks.UpdateCollectionCallback;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.services.PlayerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PlayerConnectionManager {

    public static final String PLAY_NEW_SONG = "Play new song";
    public static final String SONG_DATA = "Song data";

    private WeakReference<Context> context;
    private MusicFileSystemScanner scanner;
    private PlayerService playerService;
    boolean serviceBound = false;

    //private SongDatabase mSongDatabase;


    //private ObserverArrayList<Song> songs = new ObserverArrayList<>();
    //private List<IsMusicPlayingCallback> isMusicPlayingCallbacks = new ArrayList<>();

    //private int currentSongIndex;

    private List<IsMusicPlayingCallback> isMusicPlayingCallbacks = new ArrayList<>();
    private List<BoundToServiceCallback> boundToServiceCallbacks = new ArrayList<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            playerService = binder.getService();
            serviceBound = true;
            notifyAllBoundCallbacks();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    public PlayerConnectionManager(Context context, SongDatabase songDb, MusicFileSystemScanner scanner) {
        this.context = new WeakReference<>(context);
        this.scanner = scanner;
        //this.mSongDatabase = songDb;
    }

    public void playMedia() {
        if(!serviceBound) {
            startPlayerService(0);
        }
        else {
            playerService.playMedia();
        }
        notifyAllSubscribersOnMusicStateChange(true);
    }

    //should disable these 4 methods if playback not started yet

    public void pauseMedia() {
        playerService.pauseMedia();
        notifyAllSubscribersOnMusicStateChange(false);
    }

    public void playPrevSong() {
        if(serviceBound) {
            playerService.playPrevSong();
            notifyAllSubscribersOnMusicStateChange(true);
        }
    }

    public void playNextSong() {
        if(serviceBound) {
            playerService.playNextSong();
            notifyAllSubscribersOnMusicStateChange(true);
        }
    }

    public void playSelectedSong(int position) {
        if(!serviceBound) {
            startPlayerService(position);
        }
        else {
            playerService.playSelectedSong(position);
        }
        notifyAllSubscribersOnMusicStateChange(true);
    }

//    public void loadSongs() {
//        playerService.loadSongs();
//    }

    public void setBoundToServiceCallbacks(BoundToServiceCallback callback) {
        boundToServiceCallbacks.add(callback);
    }

    public void notifyAllBoundCallbacks() {
        for (BoundToServiceCallback callback: boundToServiceCallbacks) {
            callback.onBoundToService();
        }
    }

    public void removeBoundToServiceCallbacks(BoundToServiceCallback callback) {
        boundToServiceCallbacks.remove(callback);
    }

    public void setIsMusicPlayingCallback(IsMusicPlayingCallback isMusicPlayingCallback) {
        this.isMusicPlayingCallbacks.add(isMusicPlayingCallback);
    }

    private void notifyAllSubscribersOnMusicStateChange(boolean isPlayingState) {
        for (IsMusicPlayingCallback callback: isMusicPlayingCallbacks) {
            callback.changeMusicPlaybackState(isPlayingState);
        }
    }

    public void removeIsMusicPlayingCallback(IsMusicPlayingCallback callback) {
        isMusicPlayingCallbacks.remove(callback);
    }

//    public void setUpdateCollectionCallback(UpdateCollectionCallback callback) {
//        playerService.getSongs().setCollectionUpdateSubscribers(callback);
//    }
//
//    public void removeUpdateCollectionCallback(UpdateCollectionCallback callback) {
//        playerService.getSongs().removeFromCollectionUpdateSubscribers(callback);
//    }
//
//    public ObservableArrayList<Song> getSongs() {
//        return playerService.getSongs().getValue();
//    }

    //public ObserverArrayList<Song> getSongs() {
//        return songs;
//    }

//    public void setIsMusicPlayingCallback(IsMusicPlayingCallback isMusicPlayingCallback) {
//        this.isMusicPlayingCallbacks.add(isMusicPlayingCallback);
//    }
//
//    private void notifyAllSubscribersOnMusicStateChange(boolean isPlayingState) {
//        for (IsMusicPlayingCallback callback: isMusicPlayingCallbacks) {
//            callback.changeMusicPlaybackState(isPlayingState);
//        }
//    }


//    public void play() {
//        if(!serviceBound) {
//            startPlayerService();
//        }
//        else {
//            player.playMedia();
//        }
//        notifyAllSubscribersOnMusicStateChange(true);
//    }
//
//    public void pause() {
//        player.pauseMedia();
//        notifyAllSubscribersOnMusicStateChange(false);
//    }

//    public void playNextSong() {
//        currentSongIndex = currentSongIndex < (songs.getValue().size() - 1) ? currentSongIndex + 1 : 0;
//        playNewSong();
//    }
//
//    public void playPrevSong() {
//        currentSongIndex = currentSongIndex == 0 ? songs.getValue().size() - 1 : currentSongIndex - 1;
//        playNewSong();
//    }

//    private void playNewSong() {
//        Intent broadcastIntent = new Intent(PLAY_NEW_SONG);
//        broadcastIntent.putExtra(SONG_DATA, songs.getValue().get(currentSongIndex).getData());
//        context.get().sendBroadcast(broadcastIntent);
//        notifyAllSubscribersOnMusicStateChange(true);
//    }

//    public void playSelectedSong(int index) {
//        if (index > 0 && index < songs.getValue().size()) {
//            currentSongIndex = index;
//
//            if(!serviceBound) {
//                startPlayerService();
//            }
//            else {
//                playNewSong();
//            }
//        }
//    }

    private void startPlayerService(int position) {
        Intent playerIntent = new Intent(context.get(), PlayerService.class);
        //Song currentSong = songs.getValue().get(currentSongIndex);
        playerIntent.putExtra(PlayerService.AUDIO_FILE_DATA, position);
        //needs to be both started and bound so music will play while the app is not active
        context.get().startService(playerIntent);
        context.get().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //public void stopPlayerService() {
//        player.stopSelf();
//    }

//    public void loadSongs() {       //To be moved out (suggest to have the initial scanning activity)
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        ContentResolver cr = context.get().getContentResolver();
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        Cursor cursor = cr.query(uri, null, null, null, null );
//
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//
//                // Save to db
//                Song songToAdd = new Song(data, title, album, artist);
//                mSongDatabase.songDao().insert(songToAdd);
//                //songsLiveData.getValue().add(songToAdd);
//            }
////            songsLiveData = mSongDatabase.songDao().getAll();
//            songs.getValue().addAll(mSongDatabase.songDao().getAll());
////            songsUpdated(songs);
//            currentSongIndex = 0;
//        }
//        else {
////            List<Song> songs = new ArrayList<Song>();
////            List<Song> s = songsLiveData.getValue();
////            s.addAll(songs);
//
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//    }
}
