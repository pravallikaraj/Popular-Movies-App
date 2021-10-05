package com.example.android.popularmoviesstage1.utilities;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    public final static String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public final static String PARAM_API_KEY = "api_key";
    public final static String API_KEY = "enter your API key here";
    public final static String TRAILERS = "videos";
    public final static String REVIEWS = "reviews";




    public static String buildUrl(String moviesSearchQuery) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(moviesSearchQuery)
                .appendQueryParameter(PARAM_API_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    public static URL buildMovieTrailersUrl(String id)
    {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(TRAILERS)
                .appendQueryParameter(PARAM_API_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildMovieReviewsUrl(String id)
    {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(REVIEWS)
                .appendQueryParameter(PARAM_API_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = urlConnection.getInputStream();;
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            if (in == null) {
                return null;
            }
           reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line).append("\n");
            }
            if (stringBuffer.length()==0)
            {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if(reader!= null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        return stringBuffer.toString();
    }

}
