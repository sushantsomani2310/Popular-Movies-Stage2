package com.example.android.popularmoviess2;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmoviess2.datasource.PopularMoviesRoomDatabase;
import com.example.android.popularmoviess2.datasource.dao.FavouriteMoviesDao;
import com.example.android.popularmoviess2.model.Movie;
import com.example.android.popularmoviess2.utilities.AppExecutors;

import java.util.List;

/**
 * call to any room db method or remote API goes through this class
 * this ensures cleaner architecture by having single source of data
 * for the entire application
 */
public class PopularMoviesRepository {

    private LiveData<List<Movie>> favoriteMoviesList;
    private FavouriteMoviesDao favouriteMoviesDao;
    private LiveData<Movie> favouriteMovie;
    private AppExecutors executors;
    public PopularMoviesRepository(Application application){
        PopularMoviesRoomDatabase roomDatabase = PopularMoviesRoomDatabase.getInstance(application);
        favouriteMoviesDao = roomDatabase.favouriteMoviesDao();
        executors = AppExecutors.getInstance();
        favoriteMoviesList = favouriteMoviesDao.getFavouriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMoviesList(){
        return favoriteMoviesList;
    }

    public LiveData<Movie> determineFavoriteMovie(int movieId){
        favouriteMovie = favouriteMoviesDao.determineFavoriteMovie(movieId);
        return favouriteMovie;
    }

    public void removeAsFavoriteMovie(final int movieId){
        executors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                favouriteMoviesDao.removeFromFavourites(movieId);
            }
        });
    }
}
