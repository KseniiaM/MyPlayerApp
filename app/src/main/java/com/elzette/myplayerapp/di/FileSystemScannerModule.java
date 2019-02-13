//package com.elzette.myplayerapp.di;
//
//import android.content.Context;
//
//import com.elzette.myplayerapp.providers.MusicFileSystemScanner;
//
//import dagger.Module;
//import dagger.Provides;
//
//@Module(includes = {ContextModule.class})
//public class FileSystemScannerModule {
//
//    @Provides
//    @DatabaseApplicationScope
//    public MusicFileSystemScanner getFileSystemScanner(Context context) {
//        return new MusicFileSystemScanner(context);
//    }
//}
