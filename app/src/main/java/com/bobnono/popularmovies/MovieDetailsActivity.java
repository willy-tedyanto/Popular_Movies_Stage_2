package com.bobnono.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilties.DateUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    private MovieModel mMovie;

    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_vote_average) TextView mVoteAverage;
    @BindView(R.id.tv_overview) TextView mOverview;

    @BindView(R.id.iv_movie_poster) ImageView mMoviePoster;

    final String dateFormat = "MMMM dd, yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        mMovie = intent.getParcelableExtra(MainActivity.MOVIE_BUNDLE);

        showMovieDetails();
    }

    void showMovieDetails(){
        mMovieTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(DateUtils.calendarToString(mMovie.getReleaseDate(), dateFormat));
        mVoteAverage.setText(mMovie.getRating());
        mOverview.setText(mMovie.getOverview());

        Picasso.with(this)
                .load(mMovie.getPosterPath())
                .placeholder(R.drawable.ic_local_movies_blue_24dp)
                .error(R.drawable.ic_error_red_24dp)
                .into(mMoviePoster);
    }
}
