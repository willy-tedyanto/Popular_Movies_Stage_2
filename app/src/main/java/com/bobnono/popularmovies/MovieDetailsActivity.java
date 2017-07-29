package com.bobnono.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bobnono.popularmovies.data.MovieContract;
import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.model.ReviewModel;
import com.bobnono.popularmovies.model.TrailerModel;
import com.bobnono.popularmovies.utilities.DateUtils;
import com.bobnono.popularmovies.utilities.GeneralUtils;
import com.bobnono.popularmovies.utilities.MoviePreferences;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity
        extends AppCompatActivity
        implements TrailerAdapter.TrailerAdapterHandler, FetchTrailerTask.FetchTrailerTaskHandler,
        FetchReviewTask.FetchReviewTaskHandler {

    private MovieModel mMovie;

    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_vote_average) TextView mVoteAverage;
    @BindView(R.id.tv_overview) TextView mOverview;

    @BindView(R.id.iv_movie_poster) ImageView mMoviePoster;

    final String dateFormat = "MMMM dd, yyyy";
    final String TAG = "MovieDetailAct";

    @BindView(R.id.rv_trailers) RecyclerView mRecyclerViewTrailer;
    @BindView(R.id.tv_error_message_display_trailer) TextView mErrorMessageDisplayTrailer;
    @BindView(R.id.pb_loading_indicator_trailer) ProgressBar mLoadingIndicatorTrailer;

    @BindView(R.id.rv_reviews) RecyclerView mRecyclerViewReview;
    @BindView(R.id.tv_error_message_display_review) TextView mErrorMessageDisplayReview;
    @BindView(R.id.pb_loading_indicator_review) ProgressBar mLoadingIndicatorReview;
    @BindView(R.id.button_mark) Button mButtonMark;

    private TrailerAdapter mTrailerAdapter = null;
    private ReviewAdapter mReviewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        mRecyclerViewReview.setFocusable(false);
        mRecyclerViewTrailer.setFocusable(false);

        Intent intent = getIntent();

        mMovie = intent.getParcelableExtra(MainActivity.BUNDLE_MOVIE_DETAILS);

        LinearLayoutManager layoutManagerTrailer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerViewTrailer.setLayoutManager(layoutManagerTrailer);
        mRecyclerViewTrailer.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(MovieDetailsActivity.this, this);
        mRecyclerViewTrailer.setAdapter(mTrailerAdapter);

        LinearLayoutManager layoutManagerReview
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReview.setLayoutManager(layoutManagerReview);
        mRecyclerViewReview.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(MovieDetailsActivity.this);
        mRecyclerViewReview.setAdapter(mReviewAdapter);

        showMovieDetails();

    }


    void showMovieDetails(){
        mMovieTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(DateUtils.calendarToString(mMovie.getReleaseDate(), dateFormat));
        mVoteAverage.setText(mMovie.getRating());
        mOverview.setText(mMovie.getOverview());

        checkMovieIsFavorite();

        if (mMovie.getIsFavorite()) {
            File f = new File(MoviePreferences.POSTER_LOCAL_STORAGE_DIR
                    + "/" + getString(R.string.favorite_movie_image_folder)
                    + "/" + mMovie.getId()
                    + getString(R.string.image_poster_file_extension));

            Picasso.with(this)
                    .load(f)
                    .placeholder(R.drawable.ic_local_movies_blue_24dp)
                    .error(R.drawable.ic_error_red_24dp)
                    .into(mMoviePoster);
        } else {
            Picasso.with(this)
                    .load(mMovie.getPosterPath())
                    .placeholder(R.drawable.ic_local_movies_blue_24dp)
                    .error(R.drawable.ic_error_red_24dp)
                    .into(mMoviePoster);
        }


        loadTrailers();
        loadReviews();

    }

    private void checkMovieIsFavorite(){
        if (GeneralUtils.recordExist(this, mMovie.getId())){
            mMovie.setIsFavorite(true);
            mButtonMark.setText(R.string.mark_unfavorite);
        } else {
            mMovie.setIsFavorite(false);
            mButtonMark.setText(R.string.mark_favorite);
        }
    }

    private void loadTrailers(){
        URL url = MoviePreferences.getUrlTrailer(mMovie.getId());
        new FetchTrailerTask(this, url, mLoadingIndicatorTrailer).execute();

        showTrailersDataView();
    }

    private void loadReviews(){
        URL url = MoviePreferences.getUrlReview(mMovie.getId());
        new FetchReviewTask(this, url, mLoadingIndicatorReview).execute();

        showReviewDataView();
    }

    void showTrailersDataView(){
        mErrorMessageDisplayTrailer.setVisibility(View.INVISIBLE);
        mRecyclerViewTrailer.setVisibility(View.VISIBLE);
    }

    void showReviewDataView(){
        mErrorMessageDisplayReview.setVisibility(View.INVISIBLE);
        mRecyclerViewReview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailerAdapterClick(TrailerModel trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailer.getKey()));
        startActivity(intent);
    }

    @Override
    public void onFetchTrailerSucceed(ArrayList<TrailerModel> trailersList) {
        showTrailersDataView();
        mTrailerAdapter.setTrailersData(trailersList);
    }

    @Override
    public void onFetchTrailerError() {
        showErrorMessageTrailer();
    }

    private void showErrorMessageTrailer() {
        /* First, hide the currently visible data */
        mRecyclerViewTrailer.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplayTrailer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchReviewSucceed(ArrayList<ReviewModel> reviewList) {
        showReviewDataView();
        Log.e(TAG, "FetchReviewSuccedd : " + reviewList.size());
        mReviewAdapter.setReviewsData(reviewList);
    }

    @Override
    public void onFetchReviewError() {
        showErrorMessageReview();
    }

    private void showErrorMessageReview() {
        /* First, hide the currently visible data */
        mRecyclerViewReview.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplayReview.setVisibility(View.VISIBLE);
    }

    public void markMovie(View view){

        if (mMovie.getIsFavorite()){
            //Favorit now, make it not favorite
            // Insert the content values via a ContentResolver
            try {
                int result = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{Integer.toString(mMovie.getId())});

                if (result > 0) {
                    checkMovieIsFavorite();
                    GeneralUtils.deleteImageFavorite(this, Integer.toString(mMovie.getId()));
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        else {
            //Not favorit yet, make favorite
            ContentValues contentValues = new ContentValues();
            // Put the task description and selected mPriority into the ContentValues
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, DateUtils.calendarToString(mMovie.getReleaseDate(), "yyyy-MM-dd"));
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

            // Insert the content values via a ContentResolver
            try {
                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                if (uri != null) {
                    checkMovieIsFavorite();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            try {
                GeneralUtils.saveImageFromImageView(this, mMoviePoster, Integer.toString(mMovie.getId()));
            } catch (Exception e) {
                Log.e(TAG, "Error save image : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
