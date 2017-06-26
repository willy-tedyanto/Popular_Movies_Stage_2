package com.bobnono.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilties.DateUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private MovieModel mMovie;

    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mOverview;

    private ImageView mMoviePoster;

    final String dateFormat = "MMMM dd, yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMovieTitle = (TextView)findViewById(R.id.tv_movie_title);
        mReleaseDate = (TextView)findViewById(R.id.tv_release_date);
        mVoteAverage = (TextView)findViewById(R.id.tv_vote_average);
        mOverview = (TextView)findViewById(R.id.tv_overview);

        mMoviePoster = (ImageView)findViewById(R.id.iv_movie_poster);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mMovie = (MovieModel) bundle.getSerializable(MainActivity.MOVIE_BUNDLE);

        showMovieDetails();
    }

    void showMovieDetails(){
        mMovieTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(DateUtils.calendarToString(mMovie.getReleaseDate(), dateFormat));
        mVoteAverage.setText(mMovie.getRating());
        mOverview.setText(mMovie.getOverview());

        Picasso.with(this)
                .load(mMovie.getPosterPath())
                .into(mMoviePoster);
    }
}
