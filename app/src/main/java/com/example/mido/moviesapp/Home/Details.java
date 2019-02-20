package com.example.mido.moviesapp.Home;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mido.moviesapp.Movies.Cast.CastRecyclerAdapter;
import com.example.mido.moviesapp.Movies.Cast.Cast;
import com.example.mido.moviesapp.Movies.MoviesClient;
import com.example.mido.moviesapp.Movies.MoviesData;
import com.example.mido.moviesapp.R;
import com.example.mido.moviesapp.Utility;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Details extends AppCompatActivity {


    public static final String IMAGE_ROOT = "https://image.tmdb.org/t/p/w500";
    public String CAST_URL;
    public String DETAILS;
    String id;
    ImageView image;
    RecyclerView castView;
    RatingBar rate;

    TextView name, rateText, rateNum, genre, adult, date, status, budget, runtime, originalLang, homePage, overView;

    Bitmap imageBitmap;
    private AsyncTask mAsyncTask;
    private ProgressDialog progressDialog;
    private MoviesClient movieClient;
    private MoviesData moviesData;
    private String genreS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        id = getIntent().getExtras().getString("MovieId");
        byte[] bytes = getIntent().getExtras().getByteArray("MovieImage");
        imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        DETAILS = "" + id + "?api_key=0d88ab982b9e6e6c53658520e3948f25&language=en-US";
        CAST_URL = "https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=0d88ab982b9e6e6c53658520e3948f25";
        ini();
        if (imageBitmap != null) {
            BitmapDrawable drawable = new BitmapDrawable(getResources(), imageBitmap);
            image.setBackgroundDrawable(drawable);

        } else {
            Picasso.with(this).load(IMAGE_ROOT + getIntent().getExtras().getString("ImageUrl")).into(
                    image);

        }
        getDataFromWeb();

    }


    private void ini() {
        image = (ImageView) findViewById(R.id.mainImage);
        rate = (RatingBar) findViewById(R.id.movieRate);
        name = (TextView) findViewById(R.id.movieName);
        rateText = (TextView) findViewById(R.id.rateText);
        rateNum = (TextView) findViewById(R.id.rateVoted);
        genre = (TextView) findViewById(R.id.Genre);
        adult = (TextView) findViewById(R.id.Adult);
        date = (TextView) findViewById(R.id.ReleaseDate);
        status = (TextView) findViewById(R.id.Status);
        budget = (TextView) findViewById(R.id.Budget);
        runtime = (TextView) findViewById(R.id.Runtime);
        originalLang = (TextView) findViewById(R.id.OriginalLanguage);
        homePage = (TextView) findViewById(R.id.Homepage);
        overView = (TextView) findViewById(R.id.Overview);

    }

    void getDataFromWeb() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieClient = retrofit.create(MoviesClient.class);
        movieClient.getMovie(id,
                             getString(R.string.movie_api_key),
                             PopularFragment.DEFAULT_LANGUAGE
        ).enqueue(new Callback<MoviesData>() {
            @Override
            public void onResponse(@NonNull Call<MoviesData> call, @NonNull Response<MoviesData> response
            ) {
                moviesData = response.body();

                setMovieData();
                getCastData();

            }

            @Override
            public void onFailure(Call<MoviesData> call, Throwable t) {

            }
        });


    }

    /**
     * get Cast  and set it to Recycler to be Displayed
     */
    void getCastData() {
        movieClient.getCast(id, getString(R.string.movie_api_key))
                   .enqueue(new Callback<Cast>() {
                       @Override
                       public void onResponse(@NonNull Call<Cast> call,
                                              @NonNull Response<Cast> response
                       ) {
                           if (response.code() == PopularFragment.RESULT_OK) {
                               Cast cast = response.body();
                               castView = (RecyclerView) findViewById(R.id.Cast);
                               CastRecyclerAdapter adapter = new CastRecyclerAdapter(Details.this,
                                                                                     cast.getCast()
                               );
                               castView.setAdapter(adapter);
                               castView.setLayoutManager(new LinearLayoutManager(Details.this,
                                                                                 LinearLayout.HORIZONTAL,
                                                                                 false
                               ));
                           }
                       }

                       @Override
                       public void onFailure(Call<Cast> call, Throwable t) {
                           Log.e("CastError", t.getMessage());
                       }
                   });

    }

    /**
     * set movie data to UI View
     */
    private void setMovieData() {
        String adultS;
        if (moviesData.getAdult().equals("true")) {
            adultS = "Yes";
        } else {
            adultS = "No";
        }
        double runTime = Double.parseDouble(moviesData.getRuntime());
        double inMin = (runTime / 60);
        String runtimeS = ((int) inMin) + "h " + (int) ((inMin - ((int) inMin)) * 60) + "m";

        for (int i = 0; i < moviesData.getGenres().size(); i++) {

            MoviesData.Genres genres = moviesData.getGenres().get(i);
            if (i == 0) {
                genreS = genres.getName();
            } else {
                String s = "/" + genres.getName();
                genreS = genreS.concat(s);
            }
        }
        rate.setRating(Float.parseFloat(moviesData.getVote_average()) / 2);
        name.setText(moviesData.getOriginal_title());
        rateText.setText(moviesData.getVote_average());
        rateNum.setText(moviesData.getVote_count() + " voted");
        genre.setText(genreS);
        date.setText(moviesData.getRelease_date());
        runtime.setText(runtimeS);
        originalLang.setText(moviesData.getOriginal_language());
        overView.setText(moviesData.getOverview());
        budget.setText(moviesData.getBudget());
        homePage.setText(moviesData.getHomepage());
        adult.setText(adultS);
        status.setText(moviesData.getStatus());

        findViewById(R.id.genre_text).setMinimumWidth(Utility.calculateScreenWidthDividedBy2(this));

    }


}
