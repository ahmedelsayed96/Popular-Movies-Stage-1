package com.example.mido.moviesapp.Movies;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mido.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Callback;

/**
 * Created by Ahmed
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private final String IMAGE_ROOT = "https://image.tmdb.org/t/p/w500";
    private List<MoviesData> moviesDataList;
    private Bitmap bitmap;
    private Context context;
    private MovieClickListener movieClickListener;


    public MoviesAdapter(Context context, List<MoviesData> objects,
                         MovieClickListener movieClickListener) {
        this.context=context;
        moviesDataList = objects;
        this.movieClickListener=movieClickListener;
    }
   public interface MovieClickListener{
         void onMovieItemClickListener(int position,View view);
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.movies_raw_tmp, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return moviesDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imageView;
        private final TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
             imageView = (ImageView) itemView.findViewById(R.id.movieImage);
             name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }
        void bind(int position){
            MoviesData data=moviesDataList.get(position);
            Picasso.with(context).load(IMAGE_ROOT+data.getPoster_path()).fit().into(imageView);
            name.setText(data.getOriginal_title());
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            movieClickListener.onMovieItemClickListener(position,itemView);

        }
    }

}