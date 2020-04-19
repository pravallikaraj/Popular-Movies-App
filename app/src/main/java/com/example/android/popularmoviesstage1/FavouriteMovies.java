package com.example.android.popularmoviesstage1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.database.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmoviesstage1.R.id.recycler_view;

public class FavouriteMovies extends AppCompatActivity implements FavouriteMoviesAdapter.OnFavouriteMovieListener {

    private RecyclerView recyclerView;
    Toolbar toolbar;
    private AppDatabase mDb;
   // private ArrayList<MovieInformation> list = new ArrayList<>();
    private List<Movie> list = new ArrayList<>();
    private FavouriteMoviesAdapter favouriteMoviesAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        recyclerView = findViewById(recycler_view);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favourite Movies");

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        favouriteMoviesAdapter = new FavouriteMoviesAdapter(list,this);
        recyclerView.setAdapter(favouriteMoviesAdapter);

        mDb = AppDatabase.getInstance(getApplicationContext());
       setUpViewModel();
    }


    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovieList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.e("inside setUpViewModel", "Updating list of movies from LiveData in ViewModel");
                Log.d("TAG", "MOVIES LIST: " +movies);
                favouriteMoviesAdapter.setMovies(movies);
            }
        });
    }

        //favouriteMoviesAdapter.setMovies(mDb.favMovieDao().loadAllMovies());



    @Override
    public void onFavMovieClick(int position) {
        Log.d("MOVIE CLICKED", "POSITION: " + position);
        Log.d("TEST", "LIST: " + list);
        Log.d("TEST", "SIZE OF THE LIST: " + list.size());
        Intent intent = new Intent(FavouriteMovies.this, MovieDetailsActivity.class);
        intent.putExtra("Selected_Fav_Movie",list.get(position));
        startActivity(intent);
    }


}
