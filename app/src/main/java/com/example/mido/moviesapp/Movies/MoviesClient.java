package com.example.mido.moviesapp.Movies;

import com.example.mido.moviesapp.Movies.Cast.Cast;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ahmed
 */

public interface MoviesClient {
    @GET("popular")
    Call<Movies> getPopularMovies(@Query("api_key")String apiKey,
                                            @Query("language")String Language,
                                            @Query("page")int  pageNumber);
    @GET("top_rated")
    Call<Movies> getTopRatedMovies(@Query("api_key")String apiKey,
                                            @Query("language")String Language,
                                            @Query("page")int pageNumber);
    @GET("{id}")
    Call<MoviesData> getMovie(@Path("id")String Id,
                              @Query("api_key")String apiKey,
                              @Query("language")String Language);
    @GET("{id}/credits")
    Call<Cast> getCast(@Path("id")String Id,
                       @Query("api_key")String apiKey);

}
