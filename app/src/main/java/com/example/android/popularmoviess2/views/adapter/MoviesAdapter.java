package com.example.android.popularmoviess2.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviess2.R;
import com.example.android.popularmoviess2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{
    private List<Movie> moviesData;
    private final ListItemClickListener itemClickListener;

    public MoviesAdapter(ListItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movie_list_item,viewGroup,false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder moviesAdapterViewHolder,int index){
        moviesAdapterViewHolder.setPosterPath(index);
    }

    @Override
    public int getItemCount(){
        if(null==moviesData) return 0;
        return moviesData.size();
    }

    public void setMoviesData(List<Movie> moviesData){
        this.moviesData=moviesData;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener{
        void onListItemClick(Movie selectedMovie);
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView moviePosterImageView;
        private MoviesAdapterViewHolder(View view){
            super(view);
            moviePosterImageView = view.findViewById(R.id.movie_poster_iv);
            view.setOnClickListener(this);
        }

        private void setPosterPath(int index){
            String posterPath = moviesData.get(index).getPosterPath();
            String imagePathPartial = "http://image.tmdb.org/t/p/w185/";
            Picasso.get().load(imagePathPartial.concat(posterPath)).resize(550,1000)
                    .centerCrop()
                    .into(moviePosterImageView);
        }

        @Override
        public void onClick(View view){
            int itemPosition = getAdapterPosition();
            Movie selectedMovie = moviesData.get(itemPosition);
            itemClickListener.onListItemClick(selectedMovie);
        }
    }
}
