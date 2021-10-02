package com.example.android.popularmoviesstage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.database.Movie;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmoviesstage1.R.id.recycler_view;


public class MainActivity extends AppCompatActivity implements MovieInformationAdapter.OnMovieListener {


    // UI Components
    private RecyclerView recyclerView;
    Toolbar toolbar;
   // int movieId;
    private AppDatabase mDb;
    private Parcelable mListState;
    private String SORT_BY_OPTION = "saved_sort_order";
    private String selectedOption;
    private MenuItem menuItem;
    private String LAYOUT_STATE = "saved_layout_state";



    // variables
    private ArrayList<Movie> list = new ArrayList<>();
   // private OnFavouriteMovieListener onFavouriteMovieListener;
    private MovieInformationAdapter movieInformationAdapter;
    FavouriteMoviesAdapter favouriteMoviesAdapter;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(recycler_view);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Popular Movies");

        if(isOnline())
        {
            Log.d("TRUE","User is Connected");
        }
        else {
            Toast.makeText(MainActivity.this,"User is NOT Connected to Internet",Toast.LENGTH_SHORT).show();
            Log.d("TRUE","User is NOT Connected to Internet");
        }

        initRecyclerView();

        mDb = AppDatabase.getInstance(getApplicationContext());
       // setUpViewModel();


        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey(SORT_BY_OPTION))
            {
                selectedOption = savedInstanceState.getString(SORT_BY_OPTION);
            }
            if(savedInstanceState.containsKey(LAYOUT_STATE))
            {
                mListState = savedInstanceState.getParcelable(LAYOUT_STATE);
            }
        }

    }
//
//    private void setUpViewModel() {
//        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.getMovieList().observe(this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(List<Movie> movies) {
//                Log.e("inside setUpViewModel", "Updating list of movies from LiveData in ViewModel");
//                favouriteMoviesAdapter.notifyDataSetChanged();
//                favouriteMoviesAdapter.setMovies(movies);
//            }
//        });
//    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();
    }


    private void initRecyclerView() {
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieInformationAdapter = new MovieInformationAdapter(list,this);
        //favouriteMoviesAdapter = new FavouriteMoviesAdapter(list, FavouriteMoviesAdapter.OnFavouriteMovieListener);
        recyclerView.setAdapter(movieInformationAdapter);
        //movieInformationAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_BY_OPTION, selectedOption);
        mListState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LAYOUT_STATE, mListState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        selectedOption = savedInstanceState.getString(SORT_BY_OPTION);

        if(savedInstanceState != null)
        {
            mListState = savedInstanceState.getParcelable(LAYOUT_STATE);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//
//        if(mListState != null)
//        {
//
//            gridLayoutManager.onRestoreInstanceState(mListState);
//        }
//    }

    private void updateMoviesList(String sortBy) {
        // String url=null;

        String movieDbUrl = NetworkUtils.buildUrl(sortBy);
        new GetMoviesTask().execute(movieDbUrl);
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        //setResult(RESULT_OK, intent);
        intent.putExtra("Selected_Movie",list.get(position));
        startActivity(intent);
        //onBackPressed();


    }

    @Override
    protected void onStart() {
        updateMoviesList("popular");
        super.onStart();
    }


    public class GetMoviesTask extends AsyncTask<String, Void, Movie[]> {
        private Movie[] parseMovieInformationJson(String json) {
            Movie movieResults[] = new Movie[0];

            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray movieResultsArray = jsonObject.getJSONArray("results");
                movieResults = new Movie[movieResultsArray.length()];
                for (int i = 0; i < movieResultsArray.length(); i++) {
                    JSONObject movieObject = movieResultsArray.getJSONObject(i);
                    movieResults[i] = new Movie(movieObject.getInt("id"),
                            movieObject.getString("original_title"),
                            movieObject.getString("poster_path"),
                            movieObject.getString("overview"),
                            (float) movieObject.getDouble("vote_average"),
                            movieObject.getString("release_date"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieResults;

        }


        @Override
        protected Movie[] doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection httpURLConnection;
            String jsonStr = " ";
            try {


                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert url != null;
                jsonStr = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Movie[] movieInformation = parseMovieInformationJson(jsonStr);
            return movieInformation;

        }


        @Override
        protected void onPostExecute(Movie[] movieInformations) {
            if((movieInformations != null)&& !movieInformations.equals(""))
            {
                movieInformationAdapter.deleteMovies();
                for (Movie movieInformation : movieInformations)
                {
                    movieInformationAdapter.addMovies(movieInformation);

                }
            }

            movieInformationAdapter.notifyDataSetChanged();
            super.onPostExecute(movieInformations);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortby,menu);
        //return super.onCreateOptionsMenu(menu);
//        switch (selectedOption)
//        {
//            case R.id.action_popular:
//                menuItem = menu.findItem(R.id.action_popular);
//                menuItem.setChecked(true);
//                break;
//
//            case R.id.action_top_rated:
//                menuItem = menu.findItem(R.id.action_top_rated);
//                menuItem.setChecked(true);
//                break;
//
//            case R.id.action_favourites:
//                menuItem = menu.findItem(R.id.action_favourites);
//                menuItem.setChecked(true);
//                break;
//
//        }
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected == R.id.action_refresh)
        {
            Toast.makeText(MainActivity.this,"Page Refreshed",Toast.LENGTH_SHORT).show();
        }

        Log.d("TEST", "Testing onOptionsItemSelected");
        switch (menuItemThatWasSelected)
        {
            case R.id.action_popular:
                item.setChecked(true);
                updateMoviesList("popular");
                break;
            case  R.id.action_top_rated:
                item.setChecked(true);
                updateMoviesList("top_rated");
                break;
            case R.id.action_refresh:
                updateMoviesList("popular");
                break;
            case R.id.action_favourites:
               // favouriteMoviesAdapter.setMovies((ArrayList<Movie>) mDb.favMovieDao().loadAllMovies());
                item.setChecked(true);
                Intent intent = new Intent(MainActivity.this, FavouriteMovies.class);
                startActivity(intent);
               // setUpViewModel();
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}



