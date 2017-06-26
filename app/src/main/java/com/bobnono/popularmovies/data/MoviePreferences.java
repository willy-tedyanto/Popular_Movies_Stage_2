package com.bobnono.popularmovies.data;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by user on 2017-06-25.
 */

public class MoviePreferences {
    public enum RequestType {REQUEST_POP, REQUEST_TOP_RATED, REQUEST_DETAIL};

    private static final String DATA_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
    private static final String API_KEY_V3 = "5587113522289175ea68f9b85ed3ea23";
    private static final String KEY_PARAM = "api_key";

    private static final String POSTER_SIZE = "w185";

    public static URL getURLData(RequestType requestType, String idMovie){
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
                .appendQueryParameter(KEY_PARAM, API_KEY_V3)
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

}
