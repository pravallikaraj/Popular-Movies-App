package com.example.android.popularmoviesstage1;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.database.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();


    private LiveData<List<Movie>> movieList;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the DataBase");
        movieList = database.favMovieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovieList()
    {
        return movieList;
    }
}
