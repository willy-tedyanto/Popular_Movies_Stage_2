package com.bobnono.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.bobnono.popularmovies.data.MovieContract;
import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilities.GeneralUtils;
import com.bobnono.popularmovies.utilities.MovieDBJsonUtils;
import com.bobnono.popularmovies.utilities.MoviePreferences;
import com.bobnono.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-06-26.
 */

public class FetchMovieTask extends AsyncTask<Void, Void, ArrayList<MovieModel>> {
    private Context mContext;
    private URL url;
    private MoviePreferences.RequestType requestType;
    private ProgressBar mLoadingIndicator;

    private final FetchMoviesTaskHandler mHandler;

    public interface FetchMoviesTaskHandler{
        void onFetchMoviesSucceed(ArrayList<MovieModel> moviesList);
        void onFetchMoviesError();
    }

    public FetchMovieTask(Context context, FetchMoviesTaskHandler handler, URL url,
                          MoviePreferences.RequestType requestType, ProgressBar loadingIndicator){
        this.mContext = context;
        this.mHandler = handler;
        this.url = url;
        this.requestType = requestType;
        this.mLoadingIndicator = loadingIndicator;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<MovieModel> doInBackground(Void... params) {
        ArrayList<MovieModel> moviesList = new ArrayList<>();
        try {
            if (this.requestType == MoviePreferences.RequestType.REQUEST_TOP_RATED
                    || this.requestType == MoviePreferences.RequestType.REQUEST_POP) {

                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(this.url);

                moviesList = MovieDBJsonUtils
                        .getMovieListsFromJson(jsonMoviesResponse, this.requestType);
            }
            else { //=== Favorite movies ===
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                Cursor cursor = null;

                cursor = mContext.getContentResolver().query(uri, null, null, null, null);

                moviesList = GeneralUtils.cursorToArrayListMovieModel(cursor);
            }

            return moviesList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieModel> moviesList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (moviesList != null) {
            mHandler.onFetchMoviesSucceed(moviesList);
        } else {
            mHandler.onFetchMoviesError();
        }
    }
}
