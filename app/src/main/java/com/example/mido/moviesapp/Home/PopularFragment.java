package com.example.mido.moviesapp.Home;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mido.moviesapp.Movies.Movies;
import com.example.mido.moviesapp.Movies.MoviesAdapter;
import com.example.mido.moviesapp.Movies.MoviesClient;
import com.example.mido.moviesapp.Movies.MoviesData;
import com.example.mido.moviesapp.R;
import com.example.mido.moviesapp.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment implements MoviesAdapter.MovieClickListener {

    public static final String DEFAULT_LANGUAGE = "en-US";
    public static final int RESULT_OK = 200;
    int myNum;
    boolean isFirstTime = true;
    private View view;
    int pageNumber = 0;
    private ProgressBar progressDialog;
    private ArrayList<MoviesData> moviesList = new ArrayList<>();
    private RecyclerView moviesLGridView;
    private MoviesAdapter adapter;
    private boolean isLoading = false;
    private int dy1, visibleItemCount, totalItemCount, pastVisibleItems;
    private GridLayoutManager mLayoutManger;


    public PopularFragment() {
        // Required empty public constructor
    }

    public static PopularFragment newInstance(int someInt) {
        PopularFragment myFragment = new PopularFragment();
        Bundle args = new Bundle();
        args.putInt("Int", someInt);
        myFragment.setArguments(args);
        return myFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_popular, container, false);
        moviesLGridView = (RecyclerView) view.findViewById(R.id.moviesGridView);
        myNum = getArguments().getInt("Int");
        setListenerGrid();
        addDataFromServer();

        return view;
    }

    private void setListenerGrid() {
        moviesLGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (!isLoading) {
                    if (dy1 > 0)
                    {
                        visibleItemCount = mLayoutManger.getChildCount();
                        totalItemCount = mLayoutManger.getItemCount();
                        pastVisibleItems = mLayoutManger.findFirstVisibleItemPosition();


                        if ((visibleItemCount + pastVisibleItems) == totalItemCount && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            isLoading = true;
                            addDataFromServer();
                            Log.e("load","Loadddd");
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                dy1 = dy;
            }
        });

    }

    void addDataFromServer() {
        Retrofit retrofit = new Retrofit.Builder().
                                                          baseUrl("https://api.themoviedb.org/3/movie/").
                                                          addConverterFactory(GsonConverterFactory.create()).
                                                          build();
        MoviesClient moviesClient = retrofit.create(MoviesClient.class);
        Call<Movies> call;

        if (myNum == 0) {
            pageNumber++;
            call = moviesClient.getPopularMovies(
                    getResources().getString(R.string.movie_api_key),
                    DEFAULT_LANGUAGE,
                    pageNumber
            );

        } else {
            pageNumber++;
            call = moviesClient.getTopRatedMovies(
                    getResources().getString(R.string.movie_api_key),
                    DEFAULT_LANGUAGE,
                    pageNumber
            );
        }
        //do before getting  data
        if (isFirstTime) {
            progressDialog = (ProgressBar) view.findViewById(R.id.progress);
            progressDialog.setVisibility(View.VISIBLE);
        }
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response
            ) {

                if (response.code() == RESULT_OK) {
                    moviesList.addAll(response.body().getMoviesDatas());
                    if (isFirstTime) {
                        adapter = new MoviesAdapter(getActivity(),
                                                    moviesList,
                                                    PopularFragment.this
                        );
                        int maxNum = Utility.calculateNoOfColumns(getActivity(), 100);
                        mLayoutManger = new GridLayoutManager(getActivity(), maxNum);
                        moviesLGridView.setAdapter(adapter);
                        moviesLGridView.setLayoutManager(mLayoutManger);
                        isFirstTime = false;
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    progressDialog.setVisibility(View.GONE);
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e("getMovieError",t.getMessage());
                pageNumber--;
                if (isFirstTime) {
                    //show not internetConnection View
                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.NoConnectionLayout);
                    linearLayout.setVisibility(View.VISIBLE);
                    moviesLGridView.setVisibility(View.GONE);
                    ImageView connectionImage = (ImageView) view.findViewById(R.id.connectionImage);
                    connectionImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    });

                } else {
                    Toast.makeText(getActivity(),
                                   R.string.no_internet_connection,
                                   Toast.LENGTH_SHORT
                    ).show();
                }

            }
        });
    }

    /**
     * check internet connection
     */

    boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    @Override
    public void onMovieItemClickListener(int position, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.movieImage);
        TextView name = (TextView) view.findViewById(R.id.name);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = null;
        byte[] byteArray = null;
        if (drawable != null) {
            bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        Intent intent = new Intent(getActivity(), Details.class);
        Bundle bundle = new Bundle();
        bundle.putByteArray("MovieImage", byteArray);
        bundle.putString("MovieId", moviesList.get(position).getId());
        bundle.putString("ImageUrl", moviesList.get(position).getPoster_path());
        intent.putExtras(bundle);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> p1 = Pair.create((View) imageView,
                                                imageView.getTransitionName()
            );
            Pair<View, String> p2 = Pair.create((View) name, name.getTransitionName());
            Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                                                          p1,
                                                                          p2
            ).toBundle();
            startActivityForResult(intent, 1, bundle1);
        } else {
            startActivity(intent);

        }

    }
}
