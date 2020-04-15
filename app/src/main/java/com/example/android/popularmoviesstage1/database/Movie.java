package com.example.android.popularmoviesstage1.database;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public int movie_id;
    private String original_title;
    private String poster_path;
    private String plot_synopsis;
    private float user_rating;
    private String release_date;
    private String trailer_key;
    private String trailer_name;
    private String review_author;
    private String review_description;
    private String review_url;

    public Movie() {

    }

    @Ignore
    public Movie(String original_title, String poster_path, String plot_synopsis, float user_rating, String release_date, String trailer_key, String trailer_name, String review_author, String review_description, String review_url) {
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.plot_synopsis = plot_synopsis;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.trailer_key = trailer_key;
        this.trailer_name = trailer_name;
        this.review_author = review_author;
        this.review_description = review_description;
        this.review_url = review_url;
    }

    @Ignore
    public Movie(int movie_id, String original_title, String poster_path, String plot_synopsis, float user_rating, String release_date, String trailer_key, String trailer_name, String review_author, String review_description, String review_url) {
        this.movie_id = movie_id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.plot_synopsis = plot_synopsis;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.trailer_key = trailer_key;
        this.trailer_name = trailer_name;
        this.review_author = review_author;
        this.review_description = review_description;
        this.review_url = review_url;
    }

    public Movie(int movie_id, String original_title, String poster_path, String plot_synopsis, float user_rating, String release_date) {
        this.movie_id = movie_id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.plot_synopsis = plot_synopsis;
        this.user_rating = user_rating;
        this.release_date = release_date;

    }


    public Movie(Parcel parcel) {
        movie_id = parcel.readInt();
        original_title = parcel.readString();
        poster_path = parcel.readString();
        plot_synopsis = parcel.readString();
        user_rating = parcel.readFloat();
        release_date = parcel.readString();
        trailer_key = parcel.readString();
        trailer_name = parcel.readString();
        review_author = parcel.readString();
        review_description = parcel.readString();
        review_url = parcel.readString();
    }


    public void setMovieId(int movie_id) {
        this.movie_id = movie_id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setTrailer_key(String trailer_key) {
        this.trailer_key = trailer_key;
    }

    public void setTrailer_name(String trailer_name) {
        this.trailer_name = trailer_name;
    }

    public void setReview_author(String review_author) {
        this.review_author = review_author;
    }

    public void setReview_description(String review_description) {
        this.review_description = review_description;
    }

    public void setReview_url(String review_url) {
        this.review_url = review_url;
    }


    public int getMovie_id() {
        return movie_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTrailer_key() {
        return trailer_key;
    }

    public String getTrailer_name() {
        return trailer_name;
    }

    public String getReview_author() {
        return review_author;
    }

    public String getReview_description() {
        return review_description;
    }

    public String getReview_url() {
        return review_url;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movie_id);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(plot_synopsis);
        dest.writeFloat(user_rating);
        dest.writeString(release_date);
        dest.writeString(trailer_key);
        dest.writeString(trailer_name);
        dest.writeString(review_author);
        dest.writeString(review_description);
        dest.writeString(review_url);
    }
}