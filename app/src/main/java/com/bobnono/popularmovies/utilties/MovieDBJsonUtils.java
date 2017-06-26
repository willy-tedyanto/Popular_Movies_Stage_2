package com.bobnono.popularmovies.utilties;

import com.bobnono.popularmovies.data.MoviePreferences;
import com.bobnono.popularmovies.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-25.
 */

public class MovieDBJsonUtils {

    static final String TAG = "MovieJSONUtil";

    public static ArrayList<MovieModel> getMovieListsFromJson(String moviesJsonString, MoviePreferences.RequestType requestType)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_TITLE = "title";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        ArrayList<MovieModel> moviesList = new ArrayList<>();

        if (requestType == MoviePreferences.RequestType.REQUEST_POP
                || requestType == MoviePreferences.RequestType.REQUEST_TOP_RATED){

            JSONArray resultsArray = moviesJson.getJSONArray(MDB_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++){
                JSONObject movieResult = resultsArray.getJSONObject(i);

                MovieModel movie = new MovieModel();

                movie.setId(movieResult.getInt(MDB_ID));
                movie.setTitle(movieResult.getString(MDB_TITLE));
                movie.setPosterPath(movieResult.getString(MDB_POSTER_PATH));
                movie.setVoteAverage(movieResult.getDouble(MDB_VOTE_AVERAGE));
                movie.setOverview(movieResult.getString(MDB_OVERVIEW));
                movie.setReleaseDate(movieResult.getString(MDB_RELEASE_DATE));

                moviesList.add(movie);
            }
        }

        return moviesList;
    }
}
