package com.bobnono.popularmovies;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.bobnono.popularmovies.model.TrailerModel;
import com.bobnono.popularmovies.utilities.MovieDBJsonUtils;
import com.bobnono.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-07-28.
 */

public class FetchTrailerTask extends AsyncTask<Void, Void, ArrayList<TrailerModel>> {
    private URL url;
    private ProgressBar mLoadingIndicator;
    private final FetchTrailerTaskHandler mHandler;

    String TAG = "FetchTrailerTask";

    public interface FetchTrailerTaskHandler{
        void onFetchTrailerSucceed(ArrayList<TrailerModel> trailersList);
        void onFetchTrailerError();
    }

    public FetchTrailerTask(FetchTrailerTaskHandler handler, URL url, ProgressBar loadingIndicator){
        this.mHandler = handler;
        this.url = url;
        this.mLoadingIndicator = loadingIndicator;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<TrailerModel> doInBackground(Void... params) {
        try{
            String jsonTrailerResponse = NetworkUtils
                    .getResponseFromHttpUrl(this.url);

            ArrayList<TrailerModel> trailersList = MovieDBJsonUtils
                    .getTrailersListsFromJson(jsonTrailerResponse);
            return trailersList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<TrailerModel> trailersList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (trailersList != null) {
            mHandler.onFetchTrailerSucceed(trailersList);
        } else {
            mHandler.onFetchTrailerError();
        }
    }

}
