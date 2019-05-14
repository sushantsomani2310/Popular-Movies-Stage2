package com.example.android.popularmoviess2.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movieId}/videos")
    Call<MovieVideoResponse> getMovieVideos(@Path("movieId") int movideId, @Query("api_key") String apiKey);

    @GET("movie/{movieId}/reviews")
    Call<MovieReviewResponse> getMovieReviews(@Path("movieId") int movieId,@Query("api_key") String apiKey);
}
