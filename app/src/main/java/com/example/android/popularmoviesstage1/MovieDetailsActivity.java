package com.example.android.popularmoviesstage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.database.Movie;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MovieDetailsActivity extends AppCompatActivity {

    //int movieId;
    TextView movieTitle, releaseDate, overview;
    ImageView moviePoster, favouriteStarOn, favouriteStarOff;
    TextView rating;
    Toolbar toolbar;
    private Button playTrailer;
    private Movie movie;
    private TextView reviewsTextView, reviewContentTextView, noReviews;
    private View divider;
    private Movie[] movies;
    // private MovieInformation movieInformation;
    RecyclerView.LayoutManager layoutManager;
    private AppDatabase mDb;
    private MovieReviewsAdapter movieReviewsAdapter;
    private FavouriteMoviesAdapter favouriteMoviesAdapter;
    private RecyclerView reviewRecyclerView;
    private MovieReviews movieReviews;
   // private Menu menuItem;

    public static final String EXTRA_MOVIE_ID = "extraMovieId";
    public static final String SORT_BY = "topRated";
    private static final int DEFAULT_MOVIE_ID = -1;
    private int mMovieId = DEFAULT_MOVIE_ID;
    private ArrayList<Movie> list = new ArrayList<>();
    private ArrayList<MovieReviews> reviews = new ArrayList<>();
    private boolean isChecked;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        movieTitle = findViewById(R.id.tv_movie_tiltle);
        releaseDate = findViewById(R.id.tv_release_date);
        overview = findViewById(R.id.tv_overview);
        moviePoster = findViewById(R.id.iv_movie_poster);
        rating = findViewById(R.id.tv_rating);
        toolbar = findViewById(R.id.toolbar2);
        playTrailer = findViewById(R.id.btn_play_trailer);
        reviewsTextView = findViewById(R.id.tv_review_author);
        reviewContentTextView = findViewById(R.id.tv_review_description);
        noReviews = findViewById(R.id.tv_no_reviews);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movie Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDb = AppDatabase.getInstance(getApplicationContext());

        populateUI();



        invalidateOptionsMenu();
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("Selected_Movie"))
        {
            if(mMovieId == DEFAULT_MOVIE_ID) {
                mMovieId = intent.getIntExtra("Selected_Movie", DEFAULT_MOVIE_ID);
                MainViewModelFactory factory = new MainViewModelFactory(mDb, mMovieId);
                final MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
                viewModel.getMovie().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(Movie movie) {
                        viewModel.getMovie().removeObserver(this);
                        //list = movies;
                        populateUI();
                      //  favouriteMoviesAdapter.setMovies(movie);


                    }
                });
            }
        }
    }

    private void populateUI()
    {

        layoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManager);
        movieReviewsAdapter = new MovieReviewsAdapter(reviews);
        reviewRecyclerView.setAdapter(movieReviewsAdapter);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        if(intent != null && intent.hasExtra("Selected_Movie"))  {
            movie = intent.getParcelableExtra("Selected_Movie");
            String original_title = movie.getOriginal_title();
            String poster_path = movie.getPoster_path();
            String plot_synopsis = movie.getPlot_synopsis();
            float user_rating = movie.getUser_rating();
            String userRating = String.valueOf(user_rating);
            String release_date = movie.getRelease_date();
            String movie_id = String.valueOf(movie.getMovie_id());


            movieTitle.setText(original_title);
            Picasso.get().load("http://image.tmdb.org/t/p/w185" + poster_path).into(moviePoster);
            overview.setText(plot_synopsis);
            rating.setText(userRating + "/10");
            releaseDate.setText(release_date);
            //  reviewRecyclerView.setAdapter(movieReviewsAdapter);
            //movie = intent.getParcelableExtra("movie");
            //mDb = AppDatabase.getInstance(getApplicationContext());
            playTrailers(movie_id);
            displayReviews(movie_id);
            favouritesIcon();

        }
    }
    private void populateUI(Movie movie) {
        this.movie = movie;
        if(movie == null)
        {
            return;
        }
        populateUI();
    }

    private void favouritesIcon() {
        Intent intent = getIntent();
        if (mMovieId == DEFAULT_MOVIE_ID) {
            mMovieId = intent.getIntExtra("Selected_Movie", DEFAULT_MOVIE_ID);
            invalidateOptionsMenu();
            MainViewModelFactory factory = new MainViewModelFactory(mDb, movie.getMovie_id());
            final MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
            viewModel.getMovie().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(Movie movieInFav) {
                    viewModel.getMovie().removeObserver(this);
                    Log.d("TAG", "Receiving database update from LiveData");
                    if (movieInFav == null) {
                        invalidateOptionsMenu();
                        isChecked = true;

                    } else
                        if ((movie.getMovie_id() == movieInFav.getMovie_id()) && !isChecked) {
                        //menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.star_icon_off));
                            invalidateOptionsMenu();
                        isChecked = false;


                    } else {
                        //menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.add_to_favorites));
                            invalidateOptionsMenu();
                        isChecked = false;

                    }
                }
            });
        }

    }


    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourites, menu);
       // ImageView favOn = (ImageView) menu.findItem(R.id.action_add_favourites);
        this.menu = menu;
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        if (item.getItemId() == android.R.id.home)
        {
           // NavUtils.navigateUpFromSameTask(this);

            Toast.makeText(MovieDetailsActivity.this, "Backarrow pressed", Toast.LENGTH_SHORT).show();
            //onBackPressed();
            finish();

//            Intent intent = new Intent(MovieDetailsActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //NavUtils.navigateUpFromSameTask(this);
//            startActivity(intent);
//            finish();

//
            return true;

        }

            if (isChecked) {
            isChecked = false;
            item.setIcon(getResources().getDrawable(R.drawable.star_icon_off));
           // onFavouritesClicked();
           onFavouritesClickedForDelete();
            //item.setIcon(getResources().getDrawable(R.drawable.add_to_favorites));
            Toast.makeText(MovieDetailsActivity.this, "delete from favourites ", Toast.LENGTH_SHORT).show();
            Log.d("TEST", "action_addedto_favourites");
            return true;
        } else if (!isChecked) {
            isChecked = true;
            item.setIcon(getResources().getDrawable(R.drawable.add_to_favorites));
            item.isChecked();
           //onFavouritesClickedForDelete();
           onFavouritesClicked();
          //item.setIcon(getResources().getDrawable(R.drawable.star_icon_off));
            Toast.makeText(MovieDetailsActivity.this, "added to favourites ", Toast.LENGTH_SHORT).show();
            Log.d("TEST", "action_deletedfrom_favourites");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, "Something went wrong!.", Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //  invalidateOptionsMenu();
        //MenuItem favOn = menu.findItem(R.id.action_add_favourites);
        if (isChecked) {
            //favOn.setIcon(getResources().getDrawable(R.drawable.add_to_favorites));
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.add_to_favorites));
        } else {
            //favOn.setIcon(getResources().getDrawable(R.drawable.star_icon_off));
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.star_icon_off));
            Log.d("IN ON PREPARE", "DELETE");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void onFavouritesClickedForDelete() {
        final Movie movie;
        movie = getIntent().getExtras().getParcelable("Selected_Movie");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //isChecked = true;
                mDb.favMovieDao().deleteFavMovieById(movie.getMovie_id());
                Log.d("DELETE", "deleted from favourites");
            }
        });
    }


    public void onFavouritesClicked() {
        // ArrayList<MovieInformation> list = new ArrayList<>();
        final Movie movie;


        movie = getIntent().getExtras().getParcelable("Selected_Movie");

        //mDb.favMovieDao().addFavMovie(movie);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favMovieDao().addFavMovie(movie);
                //finish();
            }
        });
//    Toast.makeText(MovieDetailsActivity.this, "Add to Favourites", Toast.LENGTH_SHORT).show();
//    finish();

    }

    private void playTrailers(String movieId) {
        // String url=null;
        new GetMovieTrailersTask(playTrailer).execute((movieId), "videos");
        // Log.e(getClass().getName(),"TRAILERSTASK RESULT" +new GetMovieTrailersTask(playTrailer).execute((movieId), "videos"));

    }

    private void displayReviews(String movieId) {
        new GetMovieReviewsTask().execute((movieId), "reviews");
        Log.e(getClass().getName(), "REVIEWSTASK RESULT" + new GetMovieReviewsTask().execute((movieId), "reviews"));
    }


    public class GetMovieTrailersTask extends AsyncTask<String, Void, String> {

        String trailerKey[], trailerName[];
        Movie[] movies;
        private final Button playTrailer;
        public GetMovieTrailersTask(Button playTrailer) {
            this.playTrailer = playTrailer;
            this.trailerKey = trailerKey;
        }

        private String[] parseMovieTrailersInformationJson(String json) {
            //Log.d(getClass().getName(),"TRAILER SEARCH URL" +json);
            try {

                JSONObject jsonObject = new JSONObject(json);
                movies = new Movie[0];
                trailerKey = new String[0];
                JSONArray movieResultsArray = jsonObject.getJSONArray("results");
                trailerKey = new String[movieResultsArray.length()];
                trailerName = new String[movieResultsArray.length()];
                if (movieResultsArray.length() == 0) {
                    trailerKey = null;
                    trailerName = null;
                } else {
                    movies = new Movie[movieResultsArray.length()];
                    for (int i = 0; i < movieResultsArray.length(); i++) {
                       // movies[i] = new Movie();
                        trailerKey[i] = movieResultsArray.getJSONObject(i).getString("key");
                        trailerName[i] = movieResultsArray.getJSONObject(i).getString("name");

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return trailerKey;
        }


        @Override
        protected String doInBackground(String... strings) {
            String movieId = String.valueOf(movie.getMovie_id());
            //trailerKey = new String[0];
            try {
                URL trailersUrl = NetworkUtils.buildMovieTrailersUrl(movieId);
                Log.d(getClass().getName(), "TRAILERSURL" + trailersUrl);
                String trailersSearchStr = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                parseMovieTrailersInformationJson(trailersSearchStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailerKey[0];
        }

        @Override
        protected void onPostExecute(final String s) {
            //Log.e(getClass().getName(),"INSIDE POST EXECUTE" + s);
            //super.onPostExecute(s);
            playTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (s == null) {
                        playTrailer.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Sorry, no trailers here.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(trailerKey != null) {
                        watchYoutubeVideo(getApplicationContext(), s); }


                    }
                }
            });

        }
    }

    public void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (appIntent.resolveActivity(getPackageManager()) != null) {
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(webIntent);
        }
    }


    public class GetMovieReviewsTask extends AsyncTask<String, Void, MovieReviews[]> {
        String reviewAuthors[], reviewContent[], reviewsUrl[];
        MovieReviews[] movieReviews;

        private MovieReviews[] parseMovieReviewsInformationJson(String json) {
            // Movie movieResults[] = new Movie[0];

            Log.d(getClass().getName(), "REVIEWS SEARCH URL" + json);
            try {
                JSONObject jsonObject = new JSONObject(json);

                JSONArray movieReviewResultsArray = jsonObject.getJSONArray("results");
                movieReviews = new MovieReviews[movieReviewResultsArray.length()];
                reviewAuthors = new String[movieReviewResultsArray.length()];
                reviewContent = new String[movieReviewResultsArray.length()];

                if (movieReviewResultsArray.length() == 0) {
                    reviewAuthors = null;
                    reviewContent = null;
                    // reviewsUrl = null;
                } else {
                    for (int i = 0; i < movieReviewResultsArray.length(); i++) {
                        movieReviews[i] = new MovieReviews();

                        reviewAuthors[i] = movieReviewResultsArray.getJSONObject(i).getString("author");
                        Log.e("TEST", "review_author = " + reviewAuthors[i]);
                        movieReviews[i].setAuthor(reviewAuthors[i]);

                        reviewContent[i] = movieReviewResultsArray.getJSONObject(i).getString("content");
                        Log.e("TEST", "review_content = " + reviewContent[i]);
                        movieReviews[i].setContent(reviewContent[i]);

//                        reviewsUrl[i] = movieReviewResultsArray.getJSONObject(i).getString("url");
//                        Log.e("TEST","review_url = "+ reviewsUrl[i]);
//                        movieReviews[i].setUrl(reviewsUrl[i]);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("TAG", "parseMovieReviewsInformationJson returns" + movieReviews);
            return movieReviews;
        }


        @Override
        protected MovieReviews[] doInBackground(String... strings) {
            String movieId = String.valueOf(movie.getMovie_id());
            String reviewsSearchStr = " ";
            try {
                URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(movieId);
                Log.d(getClass().getName(), "REVIEWSSURL" + reviewsUrl);
                reviewsSearchStr = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                Log.d(getClass().getName(), "REVIEWSS URL SEARCH STRING" + reviewsSearchStr);
                // parseMovieReviewsInformationJson(reviewsSearchStr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            MovieReviews[] movieReviews = parseMovieReviewsInformationJson(reviewsSearchStr);
            return movieReviews;
        }

        @Override
        protected void onPostExecute(MovieReviews[] movieReviews) {
           reviewContent = new String[0];

            if(reviewContent.equals(0)) {
                reviewsTextView.setVisibility(View.GONE);
                reviewContentTextView.setVisibility(View.GONE);
                noReviews.setText("No Reviews Available");
                //noReviews.setVisibility(View.VISIBLE);

            } else if ((movieReviews != null) && !movieReviews.equals("")) {
                reviews.clear();
                reviews.addAll(Arrays.asList(movieReviews));
                for (MovieReviews movieReviews1 : movieReviews) {
                    movieReviewsAdapter.addReviews(movieReviews1);
                }

            }

            reviewRecyclerView.setNestedScrollingEnabled(false);
            movieReviewsAdapter.notifyDataSetChanged();
            super.onPostExecute(movieReviews);


        }

    }
}

