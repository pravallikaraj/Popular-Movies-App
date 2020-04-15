package com.example.android.popularmoviesstage1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.database.Movie;

public class MovieViewModel extends ViewModel {
    LiveData<Movie> movie;

    public MovieViewModel(AppDatabase database, int movieId) {
        movie = database.favMovieDao().loadFavMovieById(movieId);
    }

    public LiveData<Movie> getMovie()
    {
        return movie;
    }
}
