package com.bobnono.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.bobnono.popularmovies.utilties.MovieDBJsonUtils;
import com.bobnono.popularmovies.utilties.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterHandler{
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private MoviePreferences.RequestType mSortOrder = MoviePreferences.RequestType.REQUEST_POP;

    public static final String MOVIE_BUNDLE = "movie_bundle";

    private final String TAG = "MainActivity";
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int numOfColumns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.sort_by_pop));

        loadMoviesData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_sort_pop).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_sort_pop:
                if (mSortOrder != MoviePreferences.RequestType.REQUEST_POP){

                    mSortOrder = MoviePreferences.RequestType.REQUEST_POP;

                    actionBar.setTitle(getString(R.string.sort_by_pop));

                    loadMoviesData();

                }
                break;
            case R.id.action_sort_top_rated:
                if (mSortOrder != MoviePreferences.RequestType.REQUEST_TOP_RATED){

                    mSortOrder = MoviePreferences.RequestType.REQUEST_TOP_RATED;

                    actionBar.setTitle(getString(R.string.sort_by_top_rated));

                    loadMoviesData();
                }
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
        new FetchMoviesTask(url, mSortOrder).execute();
    }

    void showMovieDetail(MovieModel movie){
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVIE_BUNDLE, movie);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<MovieModel>> {
        private URL url;
        private MoviePreferences.RequestType requestType;

        FetchMoviesTask(URL url, MoviePreferences.RequestType requestType){
            this.url = url;
            this.requestType = requestType;
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
                showMoviesDataView();
                mMoviesAdapter.setMoviesData(moviesList);
            } else {
                showErrorMessage();
            }
        }
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
}
