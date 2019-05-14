package com.example.android.popularmoviess2.views.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmoviess2.views.adapter.MoviesAdapter;
import com.example.android.popularmoviess2.R;
import com.example.android.popularmoviess2.datasource.MovieData;
import com.example.android.popularmoviess2.model.Movie;
import com.example.android.popularmoviess2.viewmodels.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener, MovieData.MovieResponseCallback {

    private ProgressBar mProgressBar;
    private int movieSortOrder = 1;
    private MoviesAdapter moviesAdapter;
    private MainViewModel mainViewModel;
    private LiveData<List<Movie>> favoriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        moviesAdapter = new MoviesAdapter(this);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(moviesAdapter);
        mRecyclerView.setHasFixedSize(true);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mainViewModel.getMoviesList(1,this);
        favoriteMovies = mainViewModel.getFavoriteMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int selectedId = item.getItemId();
        switch (selectedId){
            case R.id.sort_top_rated :
                sortMoviesList(1);
                break;

            case R.id.sort_popularity :
                sortMoviesList(2);
                break;

            case R.id.sort_favorite :
                movieSortOrder = 3;
                favoriteMovies.observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        if(movies!=null && movies.size()!=0) movieResponseListener(movies);
                        //call snackbar method here
                        else showSnackbar(getResources().getString(R.string.no_movies_favorite),Snackbar.LENGTH_SHORT);
                    }
                });
                break;
        }
        return true;
    }

    private void showSnackbar(String message,int messageDuration){
        Snackbar notificationSnackbar = Snackbar.make(findViewById(R.id.frame_layout),message,messageDuration);
        notificationSnackbar.show();
    }

    private void sortMoviesList(int sortOrder){
        if(movieSortOrder!=sortOrder){
            mainViewModel.getMoviesList(sortOrder,this);
            movieSortOrder = sortOrder;
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.movies_already_sorted),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * checks if phone is connected to the internet of not
     * @return true if connected and false if not connected
     */
    private boolean isConnectedToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = null;
        if(connectivityManager!=null) {net = connectivityManager.getActiveNetworkInfo();}
        return net!=null && net.isConnected();
    }

    /**
     * Callback method
     * Update UI based on API response, which is running
     * in a separate thread
     * @param movieResponseList contains list of Movies based on sorting set by user
     */
    @Override
    public void movieResponseListener(List<Movie> movieResponseList) {
        mProgressBar.setVisibility(View.GONE);
        if(movieResponseList.size()!=0){
            moviesAdapter.setMoviesData(movieResponseList);
        }
    }

    /**
     * Callback method
     * Update user of any error in fetching API response
     * @param errorMessage contains error message     *
     */
    @Override
    public void notifyResponseError(String errorMessage){
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this,getResources().getString(R.string.error_message)+" "+errorMessage,Toast.LENGTH_LONG).show();
    }

    /**
     * handles movie selection from recycler view
     * @param selectedMovie contains selected movie object
     */
    @Override
    public void onListItemClick(Movie selectedMovie) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);
        detailIntent.putExtra("MovieData",selectedMovie);
        startActivity(detailIntent);
    }
}
