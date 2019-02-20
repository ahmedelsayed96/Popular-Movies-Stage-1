package com.example.mido.moviesapp.Movies.Cast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mido.moviesapp.CircleTransform;
import com.example.mido.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed
 */

public class CastRecyclerAdapter extends RecyclerView.Adapter<CastRecyclerAdapter.MyHolder> {

    Context context;
    List<CastData> list;

    public CastRecyclerAdapter(Context context, List<CastData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cast_templet, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CastData data = list.get(position);
        holder.character.setText("in " + data.getCharacter());
        holder.name.setText(data.getName());
        Picasso.with(context).load("https://image.tmdb.org/t/p/w150" + data.getProfile_path()).fit().transform(
                new CircleTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, character;

        public MyHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.castImage);
            name = (TextView) itemView.findViewById(R.id.castName);
            character = (TextView) itemView.findViewById(R.id.castCharacter);
        }
    }
}
