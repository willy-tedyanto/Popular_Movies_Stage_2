package com.bobnono.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bobnono.popularmovies.data.MoviePreferences;
import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilties.GeneralUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterHandler, FetchMovieTask.FetchMoviesTaskHandler{

    private MoviesAdapter mMoviesAdapter;

    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MoviePreferences.RequestType mSortOrder = MoviePreferences.RequestType.REQUEST_POP;

    public static final String MOVIE_BUNDLE = "movie_bundle";

    private final String TAG = "MainActivity";
    private ActionBar actionBar;

    final int GRID_SCALING_FACTOR = 180;

    static final String KEY_MOVIE_SORT_BY = "movie_sort_by";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,
                GeneralUtils.calculateNoOfColumns(this, GRID_SCALING_FACTOR)));

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        actionBar = getSupportActionBar();

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
        else{
            menuItem = menu.findItem(R.id.action_sort_top_rated);
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
  //              if (mSortOrder != MoviePreferences.RequestType.REQUEST_POP){

                    mSortOrder = MoviePreferences.RequestType.REQUEST_POP;

                    actionBar.setTitle(getString(R.string.sort_by_pop));

                    GeneralUtils.writeSharedPreference(this,
                            KEY_MOVIE_SORT_BY,
                            MoviePreferences.RequestType.REQUEST_POP.ordinal());

                    loadMoviesData();

//                }
                break;
            case R.id.action_sort_top_rated:
//                if (mSortOrder != MoviePreferences.RequestType.REQUEST_TOP_RATED){

                    mSortOrder = MoviePreferences.RequestType.REQUEST_TOP_RATED;

                    actionBar.setTitle(getString(R.string.sort_by_top_rated));

                    GeneralUtils.writeSharedPreference(this,
                            KEY_MOVIE_SORT_BY,
                            MoviePreferences.RequestType.REQUEST_TOP_RATED.ordinal());

                    loadMoviesData();
//                }
                break;
        }
        item.setChecked(true);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMoviesAdapterClick(MovieModel movie) {
        showMovieDetail(movie);
    }

    void loadMoviesData(){
        mMoviesAdapter.setMoviesData(null);  //Empty current data

        showMoviesDataView();

        URL url = MoviePreferences.getURLData(mSortOrder, "");
        new FetchMovieTask(this, url, mSortOrder, mLoadingIndicator).execute();
    }

    void showMovieDetail(MovieModel movie){
        Intent intent = new Intent(this, MovieDetailsActivity.class);

        intent.putExtra(MOVIE_BUNDLE, movie);

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
