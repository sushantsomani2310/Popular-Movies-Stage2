package com.example.android.popularmoviess2.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.popularmoviess2.datasource.dao.FavouriteMoviesDao;
import com.example.android.popularmoviess2.model.Movie;
import com.example.android.popularmoviess2.model.MovieApiService;
import com.example.android.popularmoviess2.model.MovieResponse;
import com.example.android.popularmoviess2.model.MovieReviewResponse;
import com.example.android.popularmoviess2.model.MovieVideoResponse;
import com.example.android.popularmoviess2.model.Review;
import com.example.android.popularmoviess2.model.Video;
import com.example.android.popularmoviess2.utilities.AppExecutors;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieData {

    private static final String apiKey = "";//Generate your own API key
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public interface MovieResponseCallback{
        void movieResponseListener(List<Movie> movieResponseList);
        void notifyResponseError(String errorMessage);
}

    public interface MovieVideoCallback{
        void movieVideoListener(List<Video> movieVideoResponseList);
        void movieReviewListener(List<Review> movieReviewResponseList);
    }
    /**
     * this fetches the movies list from TMDb API
     * and stores them in the Room database of the phone
     */
    public static void getTopRatedMovies(int sortOrder,final MovieResponseCallback responseCallback) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<MovieResponse> call;
        switch (sortOrder){
            case 1 ://fetch movies as per the rating order
                call = movieApiService.getTopRatedMovies(apiKey);
                break;

            case 2 ://fetch movies as per the popularity
                call = movieApiService.getMostPopularMovies(apiKey);
                break;

                default: return;
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call,@NonNull Response<MovieResponse> response) {
                if(response.code()==HttpURLConnection.HTTP_OK) {
                    if(response.body()!=null) {
                        List<Movie> listMovies = response.body().getResults();
                        responseCallback.movieResponseListener(listMovies);
                    }
                }
                else{
                    responseCallback.notifyResponseError(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call,@NonNull Throwable throwable) {
                //Handle API error here
                call.cancel();
                responseCallback.notifyResponseError("Something went wrong!!");
            }
        });
    }

    public static void fetchMovieVideos(int movieId,final MovieVideoCallback movieVideoCallback){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<MovieVideoResponse> call = movieApiService.getMovieVideos(movieId,apiKey);
        call.enqueue(new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieVideoResponse> call,@NonNull Response<MovieVideoResponse> response) {
                if(response.code()==HttpURLConnection.HTTP_OK && response.body()!=null) {
                    movieVideoCallback.movieVideoListener(response.body().getVideo());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieVideoResponse> call,@NonNull Throwable throwable) {
                //Handle API error here
                call.cancel();
                //responseCallback.notifyResponseError("Something went wrong!!");
            }
        });
    }

    /**
     * fetch movie reviews from TMDB API
     * @param movieId this is id returned by TMDB api
     * @param movieVideoCallback interface to notify at UI about the response
     */
    public static void fetchMovieReviews(int movieId,final MovieVideoCallback movieVideoCallback){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<MovieReviewResponse> call = movieApiService.getMovieReviews(movieId,apiKey);
        call.enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewResponse> call,@NonNull  Response<MovieReviewResponse> response) {
                if(response.code()==HttpURLConnection.HTTP_OK && response.body()!=null) {
                    movieVideoCallback.movieReviewListener(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewResponse> call,@NonNull Throwable throwable) {
                //Handle API error here
                call.cancel();
            }
        });
    }

    /**
     * marks a movie as favorite and store it in Room DB
     * @param favoriteMovie movie object to mark it as favorite
     * @param context is passed to instantiate Room DB
     */
    public static void setFavoriteMovies(final Movie favoriteMovie, Context context){
        AppExecutors executors = AppExecutors.getInstance();
        final PopularMoviesRoomDatabase roomDatabase = PopularMoviesRoomDatabase.getInstance(context);
        executors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavouriteMoviesDao favouriteMoviesDao = roomDatabase.favouriteMoviesDao();
                favouriteMoviesDao.insertFavouriteMovies(favoriteMovie);
            }
        });
    }
}
