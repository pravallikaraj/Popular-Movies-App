package com.example.android.popularmoviesstage1.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavMovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavMovie(Movie movie);

    @Query("DELETE FROM movie WHERE movie_id = :movieId")
    void deleteFavMovieById(int movieId);

    @Query("SELECT * FROM movie WHERE movie_id = :movieId")
    LiveData<Movie> loadFavMovieById(int movieId);


}
