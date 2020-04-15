package com.example.android.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmoviesstage1.database.Movie;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieInformationAdapter extends RecyclerView.Adapter<MovieInformationAdapter.MyViewHolder>{

    private ArrayList<Movie> list = new ArrayList<>();
    private OnMovieListener mOnMovieListener;

    public MovieInformationAdapter(ArrayList<Movie> list, OnMovieListener onMovieListener) {
        this.list = list;
        this.mOnMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_images,parent,false);
        return new MyViewHolder(view, mOnMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String moviePosterPath = list.get(position).getPoster_path();
        String poster_path_url = "http://image.tmdb.org/t/p/w342"+moviePosterPath;
        Picasso.get().load(poster_path_url).into(holder.gridItemImageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView gridItemImageView;
        OnMovieListener onMovieListener;

        public MyViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            gridItemImageView = itemView.findViewById(R.id.iv_grid_item);
            this.onMovieListener = onMovieListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());

        }
    }


    public interface OnMovieListener
    {
        void onMovieClick(int position);
    }



    public void addMovies(Movie movie)
    {
        list.add(movie);
    }
    public void deleteMovies()
    {
        list.clear();
    }


}


