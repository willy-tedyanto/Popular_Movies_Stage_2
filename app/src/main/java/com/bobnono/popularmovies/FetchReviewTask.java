package com.bobnono.popularmovies;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.bobnono.popularmovies.model.ReviewModel;
import com.bobnono.popularmovies.utilities.MovieDBJsonUtils;
import com.bobnono.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-07-28.
 */

public class FetchReviewTask extends AsyncTask<Void, Void, ArrayList<ReviewModel>> {
    private URL url;
    private ProgressBar mLoadingIndicator;
    private final FetchReviewTaskHandler mHandler;

    String TAG = "FetchReviewTask";

    public interface FetchReviewTaskHandler{
        void onFetchReviewSucceed(ArrayList<ReviewModel> reviewList);
        void onFetchReviewError();
    }

    public FetchReviewTask(FetchReviewTaskHandler handler, URL url, ProgressBar loadingIndicator){
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
    protected ArrayList<ReviewModel> doInBackground(Void... params) {
        try{
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(this.url);
            ArrayList<ReviewModel> reviewsList = MovieDBJsonUtils
                    .getReviewsListsFromJson(jsonReviewResponse);

            return reviewsList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewModel> reviewsList){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (reviewsList != null){
            mHandler.onFetchReviewSucceed(reviewsList);
        } else {
            mHandler.onFetchReviewError();
        }
    }
}
