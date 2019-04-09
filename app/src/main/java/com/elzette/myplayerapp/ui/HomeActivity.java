package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.elzette.myplayerapp.Helpers.PermissionManager;
import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.models.AlbumModel;
import com.elzette.myplayerapp.models.ArtistModel;
import com.elzette.myplayerapp.viewModels.HomeViewModel;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, NavController.OnNavigatedListener {

    public final static String LAYOUT_POSITION = "layout position";
    public final static String SERIALIZED_MODEL = "model";

    private NavController mNavController;
    private HomeViewModel mViewModel;
    private TabLayout mTabLayout;

    private HeadphonesUnpluggedReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mViewModel = ViewModelProviders.of(this)
                                       .get(HomeViewModel.class);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(this);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if(PermissionManager.requestReadExternalStoragePermission(this)) {
            mViewModel.loadMusicData();
        }

        mNavController.addOnNavigatedListener(this);
        mNavController.navigate(R.id.songStatusFragment, null, addAnimations(R.id.songStatusFragment));
    }

    public void navigateToMusicList(int layoutId) {
        mNavController.navigate(layoutId, null, addAnimations(R.id.songStatusFragment));
    }

    public void navigateToSongList(AlbumModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        mNavController.navigate(R.id.songListFragment, bundle, addAnimations(R.id.songStatusFragment));
    }

    public void navigateToSongList(ArtistModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        mNavController.navigate(R.id.songListFragment, bundle, addAnimations(R.id.songStatusFragment));
    }

    @Override
    protected void onStart() {
        subscribeToHeadphonesState();
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.REQUEST_READ_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.setReadStoragePermission(true);
                    //TODO this one will have some logic of async data loading
                    mViewModel.loadMusicData();
                } else {
                    PermissionManager.setReadStoragePermission(false);
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void subscribeToHeadphonesState() {
        IntentFilter plugReceiver = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiver = new HeadphonesUnpluggedReceiver();
        registerReceiver(receiver, plugReceiver);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                mNavController.navigate(R.id.songStatusFragment);
                break;
            case 1:
                navigateToMusicList(R.id.songListFragment);
                break;
            case 2:
                navigateToMusicList(R.id.albumListFragment);
                break;
            case 3:
                navigateToMusicList(R.id.artistListFragment);
                break;
            default:
                navigateToMusicList(R.id.songStatusFragment);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onNavigated(@NonNull NavController controller, @NonNull NavDestination destination) {
        if(destination.getId() == R.id.songStatusFragment) {
            TabLayout.Tab tab = mTabLayout.getTabAt(0);

            if(tab != null) {
                tab.select();
            }
        }
    }

    public class HeadphonesUnpluggedReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            switch (intent.getAction())
            {
                case Intent.ACTION_HEADSET_PLUG:
                    handleHeadsetPlugged(intent);
            }
        }

        private void handleHeadsetPlugged(Intent intent) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 1:
                    mViewModel.resumePlayback();
                    break;
                case 0:
                    mViewModel.pausePlayback();
                    break;
            }
        }
    }

    private NavOptions addAnimations(int popUpDestinationId) {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .setPopUpTo(popUpDestinationId, true)
                .build();
    }
//    @Override
//    public void onBackPressed() {
//        Log.d("MainActivity", "onBackPressed");
////        getSupportFragmentManager().popBackStack();
//        super.onBackPressed();
//    }
}
