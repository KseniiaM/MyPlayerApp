package com.elzette.myplayerapp.Helpers;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.services.PlayerService;

import java.lang.ref.WeakReference;
import java.util.List;

public class PlayerProvider {

    public static final String PLAY_NEW_SONG = "Play new song";
    public static final String SONG_DATA = "Song data";

    private WeakReference<Context> context;
    private PlayerService player;
    private SongDatabase mSongDatabase;

    boolean serviceBound = false;

    private ObservableArrayList<Song> songsLiveData = new ObservableArrayList<>();
    private int currentSongIndex;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    public PlayerProvider(Context context, SongDatabase songDb) {
        this.context = new WeakReference<>(context);
        this.mSongDatabase = songDb;

        getAllTracks();
    }

    public ObservableArrayList<Song> getSongs() {
        return songsLiveData;
    }

    public void play() {
        if(!serviceBound) {
            startPlayerService();
        }
        else {
            player.playMedia();
        }
    }

    public void pause() {
        player.pauseMedia();
    }

    public void playNextSong() {
        currentSongIndex = currentSongIndex < (songsLiveData.size() - 1) ? currentSongIndex + 1 : 0;
        playNewSong();
    }

    public void playPrevSong() {
        currentSongIndex = currentSongIndex == 0 ? songsLiveData.size() - 1 : currentSongIndex - 1;
        playNewSong();
    }

    private void playNewSong() {
        Intent broadcastIntent = new Intent(PLAY_NEW_SONG);
        broadcastIntent.putExtra(SONG_DATA, songsLiveData.get(currentSongIndex).getData());
        context.get().sendBroadcast(broadcastIntent);
    }

    private void startPlayerService() {
        Intent playerIntent = new Intent(context.get(), PlayerService.class);
        Song currentSong = songsLiveData.get(currentSongIndex);
        playerIntent.putExtra("media", currentSong.getData());
        context.get().startService(playerIntent);
        context.get().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopPlayerService() {
        player.stopSelf();
    }

    private void getAllTracks() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.get().getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = cr.query(uri, null, null, null, null );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to db
                Song songToAdd = new Song(data, title, album, artist);
                mSongDatabase.songDao().insert(songToAdd);
                //songsLiveData.getValue().add(songToAdd);
            }
//            songsLiveData = mSongDatabase.songDao().getAll();
            List<Song> songs;
            songs = mSongDatabase.songDao().getAll();
            songsLiveData.addAll(songs);
            currentSongIndex = 0;
        }
        else {
//            List<Song> songs = new ArrayList<Song>();
//            List<Song> s = songsLiveData.getValue();
//            s.addAll(songs);

        }
        cursor.close();
    }
}
