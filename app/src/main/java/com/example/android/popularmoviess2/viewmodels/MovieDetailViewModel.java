package com.example.android.popularmoviess2.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.android.popularmoviess2.PopularMoviesRepository;
import com.example.android.popularmoviess2.datasource.MovieData;
import com.example.android.popularmoviess2.model.Movie;

import java.util.List;

public class MovieDetailViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favoriteMoviesList;
    private LiveData<Movie> favoriteMovie;
    private PopularMoviesRepository repository;

    public MovieDetailViewModel(Application application){
        super(application);
         repository = new PopularMoviesRepository(application);
        favoriteMoviesList = repository.getFavoriteMoviesList();
    }

    public void fetchMovieVideos(int movieId, MovieData.MovieVideoCallback movieVideoCallback){
        MovieData.fetchMovieVideos(movieId,movieVideoCallback);
    }

    public void fetchMovieReviews(int movieId,MovieData.MovieVideoCallback movieVideoCallback){
        MovieData.fetchMovieReviews(movieId,movieVideoCallback);
    }

    public LiveData<Movie> isFavoriteMovie(){
        return favoriteMovie;
    }

    public void setFavoriteMovies(Movie favoriteMovie, Context context){
        MovieData.setFavoriteMovies(favoriteMovie,context);
    }

    public boolean determineFavoriteMovie(int movieId){
        favoriteMovie = repository.determineFavoriteMovie(movieId);
        if(null==favoriteMovie) return false;
        return true;
    }

    public void removeAsFavoriteMovie(int movieId){
        repository.removeAsFavoriteMovie(movieId);
    }
}
