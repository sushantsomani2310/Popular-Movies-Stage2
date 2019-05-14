package com.example.android.popularmoviess2.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmoviess2.datasource.MovieData;
import com.example.android.popularmoviess2.datasource.PopularMoviesRoomDatabase;
import com.example.android.popularmoviess2.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private PopularMoviesRoomDatabase roomDB;
    private LiveData<List<Movie>> favoriteMovies;
    public MainViewModel(Application application){
        super(application);
        roomDB = PopularMoviesRoomDatabase.getInstance(this.getApplication());
        favoriteMovies = roomDB.favouriteMoviesDao().getFavouriteMovies();
    }

    public void getMoviesList(int sortOrder, MovieData.MovieResponseCallback responseCallback){
        MovieData.getTopRatedMovies(sortOrder,responseCallback);
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        return favoriteMovies;
    }
}
