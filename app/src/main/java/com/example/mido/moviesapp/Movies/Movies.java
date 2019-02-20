package com.example.mido.moviesapp.Movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 18/09/17.
 */

public class Movies {
    @SerializedName("results")
    List<MoviesData> moviesDatas;

    public List<MoviesData> getMoviesDatas() {
        return moviesDatas;
    }
}
