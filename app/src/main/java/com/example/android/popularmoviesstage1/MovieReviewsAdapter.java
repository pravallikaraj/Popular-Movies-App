package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmoviesstage1.database.Movie;

import java.util.ArrayList;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsViewHolder> {

    private ArrayList<MovieReviews> reviews = new ArrayList<>();
//    private Movie[] movies;
//    Context context;


    public MovieReviewsAdapter(ArrayList<MovieReviews> reviews) {
        this.reviews = reviews;
    }

    public void setReviews(ArrayList<MovieReviews> reviews)
    {
        this.reviews = reviews;

    }

    @NonNull
    @Override
    public MovieReviewsAdapter.MovieReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent, false);
        return new MovieReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsAdapter.MovieReviewsViewHolder holder, int position) {
//        holder.reviewAuthor.setText(String.valueOf(movies[position].getReview_author()));
//        holder.reviewDescription.setText(String.valueOf(movies[position].getReview_description()));
        holder.bind(reviews.get(position));

    }

    @Override
    public int getItemCount() {
        if(reviews == null)
        {
            return 0;
        }
    return reviews.size();
    }


//    public int getItemCount() {
//        if(list == null)
//        {
//            return 0;
//        }
//        return list.size();
//    }

//    public int getItemCount() {
//        return list.size();
//    }


    public class MovieReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewAuthor, reviewDescription;
        public MovieReviewsViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.tv_review_author);
            reviewDescription = itemView.findViewById(R.id.tv_review_description);
        }

        private void bind(MovieReviews movieReviews)
        {
            reviewAuthor.setText(movieReviews.getAuthor());
            reviewDescription.setText(movieReviews.getContent());
        }
    }

    public void addReviews(MovieReviews movieReviews)
    {
        reviews.add(movieReviews);
    }
    public void clear()
    {
        reviews.clear();
    }
}
