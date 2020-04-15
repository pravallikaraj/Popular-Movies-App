package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmoviesstage1.database.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMoviesViewHolder> {

     private List<Movie> list = new ArrayList<>();


    //private Movie movie;
    private OnFavouriteMovieListener onFavouriteMovieListener;

    public FavouriteMoviesAdapter(List<Movie> list, OnFavouriteMovieListener onFavouriteMovieListener) {
        this.list = list;
        this.onFavouriteMovieListener = onFavouriteMovieListener;
    }

    @NonNull
    @Override
    public FavouriteMoviesAdapter.FavouriteMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_images, parent, false);
        return new FavouriteMoviesViewHolder(view, onFavouriteMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMoviesAdapter.FavouriteMoviesViewHolder holder, int position) {

        String moviePosterPath = list.get(position).getPoster_path();
        String poster_path_url = "http://image.tmdb.org/t/p/w342"+moviePosterPath;
       Picasso.get().load(poster_path_url).into(holder.gridItemImageView);

    }

    @Override
    public int getItemCount() {
        if(list == null)
        {
            return 0;
        }
        return list.size();
    }

    public List<Movie> getMovies()
    {
        return list;
    }

    public void setMovies(List<Movie> movies)
    {
        list =  movies;
        notifyDataSetChanged();
    }

    public class FavouriteMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        ImageView gridItemImageView;
        OnFavouriteMovieListener onFavouriteMovieListener;

        public FavouriteMoviesViewHolder(@NonNull View itemView, OnFavouriteMovieListener onFavouriteMovieListener) {
            super(itemView);
            gridItemImageView = itemView.findViewById(R.id.iv_grid_item);
            this.onFavouriteMovieListener = onFavouriteMovieListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFavouriteMovieListener.onFavMovieClick(getAdapterPosition());
        }
        // OnMovieListener onMovieListener;




    }
    public interface OnFavouriteMovieListener {
        void onFavMovieClick(int position);
    }

    public void addFavMovies(Movie movie)
    {
        list.add(movie);
    }
    public void deleteFavMovies()
    {
        list.clear();
    }




}
