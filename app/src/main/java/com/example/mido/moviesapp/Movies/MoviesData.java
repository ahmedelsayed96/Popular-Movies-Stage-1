package com.example.mido.moviesapp.Movies;

import java.util.List;

/**
 * Created by Ahmed
 */

public class MoviesData {
    private String poster_path,
            adult,
            overview,
            release_date,
            original_title,
            id,
            original_language,
            backdrop_path,
            popularity,
            vote_count,
            vote_average,
            homepage,
            budget,
            status,
            runtime;
    private List<Genres> genres;

    public List<Genres> getGenres() {
        return genres;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getBudget() {
        return budget;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public String getVote_average() {
        return vote_average;
    }
   public class Genres{
        String name;

        public String getName() {
            return name;
        }
    }
}
