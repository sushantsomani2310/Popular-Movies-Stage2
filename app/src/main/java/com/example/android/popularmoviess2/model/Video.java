package com.example.android.popularmoviess2.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    public String getKey(){return key;}
    public String getName(){return name;}
}
