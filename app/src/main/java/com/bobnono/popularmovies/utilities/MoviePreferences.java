package com.bobnono.popularmovies.utilities;

import android.net.Uri;
import android.os.Environment;

import com.bobnono.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by user on 2017-06-25.
 */

public class MoviePreferences {
    public enum RequestType {REQUEST_POP, REQUEST_TOP_RATED, REQUEST_FAVORITE, REQUEST_DETAIL}

    private static final String DATA_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";

    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    private static final String POSTER_SIZE = "w185";

    public static final String POSTER_LOCAL_STORAGE_DIR =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();


    private static String TAG = "MoviePreferences";

    public static URL getURLData(RequestType requestType, int page, String idMovie){
        String query = "";

        switch (requestType) {
            case REQUEST_POP:
                query = "/popular";
                break;
            case REQUEST_TOP_RATED:
                query = "/top_rated";
                break;
            case REQUEST_DETAIL:
                query = "/" + idMovie;
                break;
        }

        Uri builtUri = Uri.parse(DATA_BASE_URL + query).buildUpon()
                .appendQueryParameter(KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getURLPoster(String posterPath){
        return POSTER_BASE_URL + "/" + POSTER_SIZE + "/" + posterPath;
    }

    public static URL getUrlTrailer(int id){
        Uri builtUri = Uri.parse(DATA_BASE_URL + "/" + id + "/videos").buildUpon()
                .appendQueryParameter(KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getUrlReview(int id){
        Uri builtUri = Uri.parse(DATA_BASE_URL + "/" + id + "/reviews").buildUpon()
                .appendQueryParameter(KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
