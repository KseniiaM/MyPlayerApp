//package com.elzette.myplayerapp.di;
//
//import android.arch.persistence.room.Room;
//import android.content.Context;
//
//import com.elzette.myplayerapp.dal.SongDatabase;
//
//import dagger.Module;
//import dagger.Provides;
//
//@Module(includes = ContextModule.class)
//public class DatabaseModule {
//
//    @Provides
//    public SongDatabase getDatabase(Context context) {
//        return  Room.databaseBuilder(context, SongDatabase.class, "database")
//                    .allowMainThreadQueries()
//                    .build();
//    }
//}
