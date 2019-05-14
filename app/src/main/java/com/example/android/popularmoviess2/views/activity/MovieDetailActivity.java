package com.example.android.popularmoviess2.views.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviess2.model.Review;
import com.example.android.popularmoviess2.views.adapter.MovieReviewAdapter;
import com.example.android.popularmoviess2.views.adapter.MovieTrailerAdapter;
import com.example.android.popularmoviess2.datasource.MovieData;
import com.example.android.popularmoviess2.model.Movie;
import com.example.android.popularmoviess2.R;
import com.example.android.popularmoviess2.model.Video;
import com.example.android.popularmoviess2.viewmodels.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements MovieData.MovieVideoCallback, MovieTrailerAdapter.ListItemClickListener,MovieReviewAdapter.MovieReviewClickListener {

    private RecyclerView movieVideoRecyclerView,movieReviewRecyclerView;
    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;
    private TextView movieNameTv,voteAverageTv,movieOverviewTv,releaseDateTv,movieTrailersTv,movieReviewsTv;
    private ImageView moviePosterIv,favoriteIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //initialize variables
        //mBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_detail);
        movieTrailersTv = findViewById(R.id.trailers_label_tv);
        moviePosterIv = findViewById(R.id.movie_poster_iv);
        movieNameTv = findViewById(R.id.movie_name_tv);
        voteAverageTv = findViewById(R.id.vote_average_tv);
        movieOverviewTv = findViewById(R.id.movie_overview_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        movieVideoRecyclerView = findViewById(R.id.movie_trailer_rv);
        movieReviewRecyclerView = findViewById(R.id.movie_review_rv);
        movieReviewsTv = findViewById(R.id.reviews_label_tv);
        favoriteIv = findViewById(R.id.favorite_image_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        movieVideoRecyclerView.setLayoutManager(layoutManager);
        movieVideoRecyclerView.setHasFixedSize(true);
        movieTrailerAdapter = new MovieTrailerAdapter(this);
        movieVideoRecyclerView.setAdapter(movieTrailerAdapter);

        //review adapter initialization
        movieReviewAdapter = new MovieReviewAdapter(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        movieReviewRecyclerView.setLayoutManager(layoutManager2);
        movieReviewRecyclerView.setHasFixedSize(true);
        movieReviewRecyclerView.setAdapter(movieReviewAdapter);

        final Movie movie = (Movie) getIntent().getSerializableExtra("MovieData");
        final MovieDetailViewModel movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.fetchMovieVideos(movie.getId(),this);
        movieDetailViewModel.fetchMovieReviews(movie.getId(),this);
        String imagePathPartial = "http://image.tmdb.org/t/p/w185/";
        String posterPath = imagePathPartial.concat(movie.getPosterPath());
        Picasso.get().load(posterPath).into(moviePosterIv);
        movieNameTv.append(movie.getOriginalTitle()+"\n\n\n");
        voteAverageTv.append(movie.getVoteAverage()+"\n\n\n");
        movieOverviewTv.append(movie.getOverview()+"\n\n\n");
        releaseDateTv.append(movie.getReleaseDate() + "\n\n");

        //makes the movie favorite or if movie is already in favorite list
        //then it removes the movie from the favorite list
        movieDetailViewModel.determineFavoriteMovie(movie.getId());
        movieDetailViewModel.isFavoriteMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie favouriteMovies) {
                if(null!=favouriteMovies) {
                    favoriteIv.setImageResource(R.drawable.ic_favorite_red_48dp);
                    movie.setFavorite(true);
                }
                else movie.setFavorite(false);

            }
        });
        favoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie.getFavorite()){
                    //remove favorite movie from Room
                    movieDetailViewModel.removeAsFavoriteMovie(movie.getId());
                    favoriteIv.setImageResource(R.drawable.ic_favorite_border_grey_48dp);
                    Snackbar notificationSnackbar = Snackbar.make(findViewById(R.id.constraint_layout),getResources().getString(R.string.removed_from_favorite),Snackbar.LENGTH_SHORT);
                    notificationSnackbar.show();
                }
                else{
                    //set as favorite in Room
                    Movie favouriteMovies = new Movie(movie.getPosterPath(),movie.getOverview(),movie.getReleaseDate(),movie.getId(),
                    movie.getOriginalTitle(),movie.getVoteAverage());
                    movieDetailViewModel.setFavoriteMovies(favouriteMovies,MovieDetailActivity.this);
                    favoriteIv.setImageResource(R.drawable.ic_favorite_red_48dp);
                    Snackbar notificationSnackbar = Snackbar.make(findViewById(R.id.constraint_layout),getResources().getString(R.string.set_as_favorite),Snackbar.LENGTH_SHORT);
                    notificationSnackbar.show();
                }
            }
        });
    }

    @Override
    public void movieVideoListener(List<Video> movieVideoResponseList) {
        if(movieVideoResponseList.size()!=0) movieTrailerAdapter.setMovieTrailerList(movieVideoResponseList);
        else movieTrailersTv.setVisibility(View.GONE);
    }

    @Override
    public void movieReviewListener(List<Review> movieReviewResponseList) {
        if(movieReviewResponseList.size()!=0) movieReviewAdapter.setMovieReviews(movieReviewResponseList);
        else movieReviewsTv.setVisibility(View.GONE);
    }

    /**
     * callback method when a video is selected in Recycler View
     * @param selectedVideo Video object selected by user
     */
    @Override
    public void onItemSelected(Video selectedVideo) {
        String videoKey = selectedVideo.getKey();
        Intent videoIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+videoKey));
        if(videoIntent.resolveActivity(getPackageManager())!=null) startActivity(videoIntent);
    }

    /**
     * callback method when a review is selected in its Recycler View
     * @param selectedReview Review object selected by user
     */
    @Override
    public void onReviewItemSelected(Review selectedReview) {
        Intent reviewIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(selectedReview.getUrl()));
        if(reviewIntent.resolveActivity(getPackageManager())!=null){
            startActivity(reviewIntent);
        }
    }
}
