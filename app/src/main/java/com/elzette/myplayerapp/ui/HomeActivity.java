package com.elzette.myplayerapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.elzette.myplayerapp.Helpers.PermissionManager;
import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.models.AlbumModel;
import com.elzette.myplayerapp.models.ArtistModel;
import com.elzette.myplayerapp.ui.pager.PagerAdapter;
import com.elzette.myplayerapp.viewModels.HomeViewModel;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

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

        mNavController.navigate(R.id.songStatusFragment);
    }

    public void navigateToSongList(int tabPosition) {
        mNavController.navigate(R.id.songListFragment, createBundle(tabPosition), addAnimations());
    }

    public void navigateToSongList(AlbumModel model) {
        Bundle bundle = createBundle(1);
        bundle.putSerializable("model", model);
        mNavController.navigate(R.id.songListFragment, bundle, addAnimations());
    }

    public void navigateToSongList(ArtistModel model) {
        Bundle bundle = createBundle(1);
        bundle.putSerializable("model", model);
        mNavController.navigate(R.id.songListFragment, bundle, addAnimations());
    }

    private Bundle createBundle( int tabPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_POSITION, tabPosition);
        return bundle;
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
                navigateToSongList(1);
                break;
            case 2:
                navigateToSongList(2);
                break;
            case 3:
                navigateToSongList(3);
                break;
            default:
                navigateToSongList(1);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

    private NavOptions addAnimations() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build();
    }
//    @Override
//    public void onBackPressed() {
//        Log.d("MainActivity", "onBackPressed");
////        getSupportFragmentManager().popBackStack();
//        super.onBackPressed();
//    }
}
