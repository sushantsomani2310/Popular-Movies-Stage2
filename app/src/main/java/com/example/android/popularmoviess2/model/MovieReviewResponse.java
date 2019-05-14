package com.example.android.popularmoviess2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Review> results;

    public MovieReviewResponse(int id,List<Review> results){
        this.id=id;
        this.results=results;
    }

    public List<Review> getResults(){return results;}
}
