package com.example.android.popularmoviess2.views.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviess2.R;
import com.example.android.popularmoviess2.model.Video;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder>{

    private List<Video> movieTrailerList;
    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener{
        void onItemSelected(Video selectedVideo);
    }

    public MovieTrailerAdapter(ListItemClickListener listItemClickListener){
        this.listItemClickListener=listItemClickListener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_trailer_item,viewGroup,false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder movieTrailerViewHolder, int i) {
        movieTrailerViewHolder.videoNameTextView.setText(movieTrailerList.get(i).getName());
        movieTrailerViewHolder.videoPlayImageView.setImageResource(R.drawable.ic_play_arrow_black_48dp);
    }

    @Override
    public int getItemCount() {
        if(movieTrailerList!=null) return movieTrailerList.size();
        return 0;
    }

    public void setMovieTrailerList(List<Video> movieTrailerList){
        this.movieTrailerList=movieTrailerList;
        notifyDataSetChanged();
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView videoNameTextView;
        private ImageView videoPlayImageView;
        private MovieTrailerViewHolder(View view){
            super(view);
            videoNameTextView = view.findViewById(R.id.video_name_tv);
            videoPlayImageView = view.findViewById(R.id.video_player_iv);
            videoPlayImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == videoPlayImageView.getId()){
                int clickedItemIndex = getAdapterPosition();
                Video selectedVideo = movieTrailerList.get(clickedItemIndex);
                listItemClickListener.onItemSelected(selectedVideo);
            }
        }
    }
}
