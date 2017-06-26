package com.bobnono.popularmovies;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.bobnono.popularmovies.data.MoviePreferences;
import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilties.MovieDBJsonUtils;
import com.bobnono.popularmovies.utilties.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-06-26.
 */

public class FetchMovieTask extends AsyncTask<Void, Void, ArrayList<MovieModel>> {
    private URL url;
    private MoviePreferences.RequestType requestType;
    private ProgressBar mLoadingIndicator;

    private final FetchMoviesTaskHandler mHandler;

    public interface FetchMoviesTaskHandler{
        void onFetchMoviesSucceed(ArrayList<MovieModel> moviesList);
        void onFetchMoviesError();
    }

    public FetchMovieTask(FetchMoviesTaskHandler handler, URL url,
                          MoviePreferences.RequestType requestType, ProgressBar loadingIndicator){
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
        try {
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(this.url);

            ArrayList<MovieModel> moviesList = MovieDBJsonUtils
                    .getMovieListsFromJson(jsonMoviesResponse, this.requestType);

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
