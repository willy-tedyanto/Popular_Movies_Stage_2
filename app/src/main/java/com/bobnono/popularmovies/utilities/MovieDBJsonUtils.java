package com.bobnono.popularmovies.utilities;

import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.model.ReviewModel;
import com.bobnono.popularmovies.model.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-25.
 */

public class MovieDBJsonUtils {

    static final String TAG = "MovieJSONUtil";

    public static ArrayList<MovieModel> getMovieListsFromJson(String moviesJsonString,
                                                              MoviePreferences.RequestType requestType)
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


    public static ArrayList<TrailerModel> getTrailersListsFromJson(String trailersJsonString)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_ISO_639_1 = "iso_639_1";
        final String MDB_ISO_3166_1 = "iso_3166_1";
        final String MDB_KEY = "key";
        final String MDB_NAME = "name";
        final String MDB_SITE = "site";
        final String MDB_SIZE = "size";
        final String MDB_TYPE = "type";

        JSONObject trailersJson = new JSONObject(trailersJsonString);
        ArrayList<TrailerModel> trailersList = new ArrayList<>();

        JSONArray resultsArray = trailersJson.getJSONArray(MDB_RESULTS);

        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject trailerResult = resultsArray.getJSONObject(i);

            try{

                TrailerModel trailer = new TrailerModel();

                trailer.setId(trailerResult.getString(MDB_ID));
                trailer.setIso_639_1(trailerResult.getString(MDB_ISO_639_1));
                trailer.setIso_3166_1(trailerResult.getString(MDB_ISO_3166_1));
                trailer.setKey(trailerResult.getString(MDB_KEY));
                trailer.setName(trailerResult.getString(MDB_NAME));
                trailer.setSite(trailerResult.getString(MDB_SITE));
                trailer.setSize(trailerResult.getInt(MDB_SIZE));
                trailer.setType(trailerResult.getString(MDB_TYPE));

                trailersList.add(trailer);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
        return trailersList;
    }

    public static ArrayList<ReviewModel> getReviewsListsFromJson(String reviewsJsonString)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_AUTHOR = "author";
        final String MDB_CONTENT = "content";
        final String MDB_URL = "url";

        JSONObject reviewsJson = new JSONObject(reviewsJsonString);
        ArrayList<ReviewModel> reviewsList = new ArrayList<>();

        JSONArray resultsArray = reviewsJson.getJSONArray(MDB_RESULTS);

        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject reviewResult = resultsArray.getJSONObject(i);

            try{
                ReviewModel review = new ReviewModel();

                review.setId(reviewResult.getString(MDB_ID));
                review.setAuthor(reviewResult.getString(MDB_AUTHOR));
                review.setContent(reviewResult.getString(MDB_CONTENT));
                review.setUrl(reviewResult.getString(MDB_URL));
                reviewsList.add(review);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
        return reviewsList;
    }

}
