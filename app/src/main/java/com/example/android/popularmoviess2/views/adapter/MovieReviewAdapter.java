package com.example.android.popularmoviess2.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviess2.R;
import com.example.android.popularmoviess2.model.Review;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private List<Review> movieReviewResponseList;
    private MovieReviewClickListener movieReviewClickListener;
    public interface MovieReviewClickListener{
        void onReviewItemSelected(Review selectedReview);
    }

    public MovieReviewAdapter(MovieReviewClickListener movieReviewClickListener){
        this.movieReviewClickListener = movieReviewClickListener;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movie_review_item,viewGroup,false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder movieReviewViewHolder, int i) {
        movieReviewViewHolder.reviewerAuthorNameTv.setText(movieReviewResponseList.get(i).getAuthor());
        String rawReview = movieReviewResponseList.get(i).getContent();
        int limit = 100; //denotes the word limit of review of the movie
        if(rawReview.length()>limit){
            rawReview = rawReview.substring(0,(limit-1));
        }
        String text = "<a type='text/html' href='"+movieReviewResponseList.get(i).getUrl()+"'>Read more</a>";
        movieReviewViewHolder.reviewTextTv.append(rawReview+"... "+Html.fromHtml(text));
        //movieReviewViewHolder.reviewTextTv.setText(rawReview+"....");
    }

    @Override
    public int getItemCount() {
        if(null!=movieReviewResponseList) return movieReviewResponseList.size();
        return 0;
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView reviewerAuthorNameTv,reviewTextTv;

        private MovieReviewViewHolder(View view){
            super(view);
            reviewerAuthorNameTv = view.findViewById(R.id.review_author_name_tv);
            reviewTextTv = view.findViewById(R.id.movie_review_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            Review selectedReview = movieReviewResponseList.get(getAdapterPosition());
            movieReviewClickListener.onReviewItemSelected(selectedReview);
        }
    }

    public void setMovieReviews(List<Review> movieReviewResponseList){
        this.movieReviewResponseList=movieReviewResponseList;
        notifyDataSetChanged();
    }
}
