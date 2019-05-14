package com.example.android.popularmoviess2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Video> video;

    public List<Video> getVideo(){return video;}
    public int getId(){return id;}
}
