package com.example.android.popularmoviess2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "FavouriteMovies")
public class Movie implements Serializable {

    @ColumnInfo(name = "moviePosterUrl")
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "releaseDate")
    @SerializedName("release_date")
    private String releaseDate;

    @PrimaryKey
    @SerializedName("id")
    private Integer id;

    @ColumnInfo(name = "title")
    @SerializedName("original_title")
    private String originalTitle;

    @ColumnInfo(name = "userRating")
    @SerializedName("vote_average")
    private Double voteAverage;

    private boolean isFavorite;

    public Movie(String posterPath, String overview, String releaseDate, Integer id,
                 String originalTitle, Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }
    public String getOverview() {
        return overview;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public Double getVoteAverage() {
        return voteAverage;
    }
    public void setFavorite(boolean isFavorite){this.isFavorite=isFavorite;}
    public boolean getFavorite(){return isFavorite;}
}
