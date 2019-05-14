package com.example.android.popularmoviess2.datasource;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.popularmoviess2.datasource.dao.FavouriteMoviesDao;
import com.example.android.popularmoviess2.model.Movie;

@Database(entities = {Movie.class},version = 2)
public abstract class PopularMoviesRoomDatabase extends RoomDatabase {
    private static volatile PopularMoviesRoomDatabase sInstance;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME="POPULAR_MOVIES_DATABASE";

    public static PopularMoviesRoomDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context,PopularMoviesRoomDatabase.class,
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavouriteMoviesDao favouriteMoviesDao();
}
