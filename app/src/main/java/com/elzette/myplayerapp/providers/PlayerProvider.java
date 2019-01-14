package com.elzette.myplayerapp.providers;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.elzette.myplayerapp.Helpers.PermissionManager;
import com.elzette.myplayerapp.dal.Song;
import com.elzette.myplayerapp.dal.SongDatabase;
import com.elzette.myplayerapp.services.PlayerService;

import java.lang.ref.WeakReference;

public class PlayerProvider {

    public static final String PLAY_NEW_SONG = "Play new song";
    public static final String SONG_DATA = "Song data";

    private WeakReference<Context> context;
    private PlayerService player;
    private SongDatabase mSongDatabase;

    boolean serviceBound = false;

    private ObservableArrayList<Song> songs = new ObservableArrayList<>();

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

        if(PermissionManager.checkIfReadStoragePermissionIsGranted())
        {
            loadSongs();
        }
    }

    public ObservableArrayList<Song> getSongs() {
        return songs;
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
        currentSongIndex = currentSongIndex < (songs.size() - 1) ? currentSongIndex + 1 : 0;
        playNewSong();
    }

    public void playPrevSong() {
        currentSongIndex = currentSongIndex == 0 ? songs.size() - 1 : currentSongIndex - 1;
        playNewSong();
    }

    private void playNewSong() {
        Intent broadcastIntent = new Intent(PLAY_NEW_SONG);
        broadcastIntent.putExtra(SONG_DATA, songs.get(currentSongIndex).getData());
        context.get().sendBroadcast(broadcastIntent);
    }

    public void playSelectedSong(int index) {
        if (index > 0 && index < songs.size()) {
            currentSongIndex = index;
            playNewSong();
        }
    }

    private void startPlayerService() {
        Intent playerIntent = new Intent(context.get(), PlayerService.class);
        Song currentSong = songs.get(currentSongIndex);
        playerIntent.putExtra("media", currentSong.getData());
        //needs to be both started and bound so music will play while the app is not active
        context.get().startService(playerIntent);
        context.get().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopPlayerService() {
        player.stopSelf();
    }

    public void loadSongs() {       //To be moved out (suggest to have the initial scanning activity)
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
            songs.addAll(mSongDatabase.songDao().getAll());
//            songsUpdated(songs);
            currentSongIndex = 0;
        }
        else {
//            List<Song> songs = new ArrayList<Song>();
//            List<Song> s = songsLiveData.getValue();
//            s.addAll(songs);

        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
