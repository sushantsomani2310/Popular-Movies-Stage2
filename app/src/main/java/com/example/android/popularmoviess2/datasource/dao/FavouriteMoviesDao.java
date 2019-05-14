package com.example.android.popularmoviess2.datasource.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.popularmoviess2.model.Movie;

import java.util.List;

@Dao
public interface FavouriteMoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMovies(Movie favouriteMovie);

    @Query("Select * FROM FavouriteMovies")
    LiveData<List<Movie>> getFavouriteMovies();

    @Query("Delete FROM FavouriteMovies where id = :movieId")
    void removeFromFavourites(int movieId);

    @Query("Select * FROM FavouriteMovies where id = :movieId")
    LiveData<Movie> determineFavoriteMovie(int movieId);
}
