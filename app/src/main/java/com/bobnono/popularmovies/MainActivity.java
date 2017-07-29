package com.bobnono.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bobnono.popularmovies.utilities.MoviePreferences;
import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilities.GeneralUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterHandler, FetchMovieTask.FetchMoviesTaskHandler{

    private MoviesAdapter mMoviesAdapter = null;
    private EndlessRecyclerViewScrollListener scrollListener;

    final int GRID_SCALING_FACTOR = 180;
    final String KEY_MOVIE_SORT_BY = "movie_sort_by";

    public static final String BUNDLE_MOVIE_DETAILS = "bundle_movie_details";
    final String BUNDLE_MOVIES_DATA = "bundle_movie_data";

    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;


    private final String TAG = "MainActivity";
    private ActionBar actionBar;

    private static MoviePreferences.RequestType mRequestBy;

    static int gridLastPosition = -1;   //Store grid position before start another activity
    static boolean isActivityReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

Log.e(TAG, "ONcreate New");
        ButterKnife.bind(this);

        mMoviesAdapter = new MoviesAdapter(MainActivity.this, this);

        if (!(savedInstanceState == null) && !(savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES_DATA) == null)){
            ArrayList<MovieModel> moviesData = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES_DATA);
            Log.e(TAG, "Moviesdata size : " + Integer.toString(moviesData.size()));

            mMoviesAdapter.setMoviesData(moviesData);
            isActivityReload = true;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                GeneralUtils.calculateNoOfColumns(this, GRID_SCALING_FACTOR));

        mRecyclerView.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Load next page of movies
                if (mRequestBy != MoviePreferences.RequestType.REQUEST_FAVORITE) {
                    int itemPerPage = 20;
                    loadMoviesData(mMoviesAdapter.getItemCount() / itemPerPage + 1, mRequestBy);
                }
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mMoviesAdapter);

        actionBar = getSupportActionBar();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gridLastPosition = ((GridLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        isActivityReload = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRecyclerView.getLayoutManager().scrollToPosition(gridLastPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_MOVIES_DATA, mMoviesAdapter.getMoviesData());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        int sortBy = GeneralUtils.readSharedPreferences(this,
                KEY_MOVIE_SORT_BY,
                MoviePreferences.RequestType.REQUEST_POP.ordinal());

        if (sortBy == MoviePreferences.RequestType.REQUEST_POP.ordinal()) {
            menuItem = menu.findItem(R.id.action_sort_pop);
            menuItem.setChecked(true);
        }
        else if (sortBy == MoviePreferences.RequestType.REQUEST_TOP_RATED.ordinal()) {
            menuItem = menu.findItem(R.id.action_sort_top_rated);
            menuItem.setChecked(true);
        }
        else {
            menuItem = menu.findItem(R.id.action_sort_favorite);
            menuItem.setChecked(true);
        }

        onOptionsItemSelected(menuItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_sort_pop:

                actionBar.setTitle(getString(R.string.sort_by_pop));

                GeneralUtils.writeSharedPreference(this,
                        KEY_MOVIE_SORT_BY,
                        MoviePreferences.RequestType.REQUEST_POP.ordinal());

                mRequestBy = MoviePreferences.RequestType.REQUEST_POP;

                break;
            case R.id.action_sort_top_rated:

                actionBar.setTitle(getString(R.string.sort_by_top_rated));

                GeneralUtils.writeSharedPreference(this,
                        KEY_MOVIE_SORT_BY,
                        MoviePreferences.RequestType.REQUEST_TOP_RATED.ordinal());

                mRequestBy = MoviePreferences.RequestType.REQUEST_TOP_RATED;

                break;
            case R.id.action_sort_favorite:
                actionBar.setTitle(getString(R.string.sort_by_favorite));
                GeneralUtils.writeSharedPreference(this,
                        KEY_MOVIE_SORT_BY,
                        MoviePreferences.RequestType.REQUEST_FAVORITE.ordinal());

                mRequestBy = MoviePreferences.RequestType.REQUEST_FAVORITE;
        }

Log.e(TAG, "onOptionItemSelected " + mRequestBy);

        loadMoviesData(1, mRequestBy);

        item.setChecked(true);

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMoviesAdapterClick(MovieModel movie) {
        showMovieDetail(movie);
    }

    void loadMoviesData(int page, MoviePreferences.RequestType sortBy){
        if (isActivityReload) {
            mMoviesAdapter.notifyDataSetChanged();
            isActivityReload = false;
        }
        else {
            if (page == 1) {
                mMoviesAdapter.setMoviesData(null);  //Empty current data
                scrollListener.resetState();
            }
            URL url = MoviePreferences.getURLData(sortBy, page, "");
            new FetchMovieTask(MainActivity.this, this, url, sortBy, mLoadingIndicator).execute();
        }
        showMoviesDataView();

    }


    void showMovieDetail(MovieModel movie){
        Intent intent = new Intent(this, MovieDetailsActivity.class);

        intent.putExtra(BUNDLE_MOVIE_DETAILS, movie);

        startActivity(intent);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    void showMoviesDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchMoviesSucceed(ArrayList<MovieModel> moviesList) {
        showMoviesDataView();
        mMoviesAdapter.setMoviesData(moviesList);

    }

    @Override
    public void onFetchMoviesError() {
        showErrorMessage();
    }

}
