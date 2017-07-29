package com.bobnono.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bobnono.popularmovies.model.MovieModel;
import com.bobnono.popularmovies.utilities.MoviePreferences;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 2017-06-25.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private final String TAG = "MoviesAdapter";

    private Context mContext;
    private ArrayList<MovieModel> mMovies;

    private final MoviesAdapterHandler mHandler;

    public interface MoviesAdapterHandler{
        void onMoviesAdapterClick(MovieModel movie);
    }

    public MoviesAdapter(Context context, MoviesAdapterHandler handler){
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutIdForGridItem = R.layout.movies_poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Boolean isFavorite = true;

        if (mMovies.get(position).getIsFavorite() == null || !mMovies.get(position).getIsFavorite()){
            isFavorite = false;
        }

        if (isFavorite) {
            File f = new File(MoviePreferences.POSTER_LOCAL_STORAGE_DIR
                    + "/" + mContext.getString(R.string.favorite_movie_image_folder)
                    + "/" + mMovies.get(position).getId()
                    + mContext.getString(R.string.image_poster_file_extension));

            Picasso.with(mContext)
                    .load(f)
                    .placeholder(R.drawable.ic_local_movies_blue_24dp)
                    .error(R.drawable.ic_error_red_24dp)
                    .into(holder.mMovieImageView);
        } else {
            Picasso.with(mContext)
                    .load(mMovies.get(position).getPosterPath())
                    .placeholder(R.drawable.ic_local_movies_blue_24dp)
                    .error(R.drawable.ic_error_red_24dp)
                    .into(holder.mMovieImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    public class MoviesAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public final ImageView mMovieImageView;

        public MoviesAdapterViewHolder(View view){
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieModel movie = mMovies.get(adapterPosition);
            mHandler.onMoviesAdapterClick(movie);
        }
    }

    public int getMoviesCount(){
        return mMovies.size();
    }

    public ArrayList<MovieModel> getMoviesData(){
        return mMovies;
    }

    public void setMoviesData(ArrayList<MovieModel> movies){
        if (movies == null){
            mMovies = null;
        }
        else {
            if (mMovies == null){
                mMovies = movies;
            }
            else {
                mMovies.addAll(movies);
            }
        }
        notifyDataSetChanged();
    }
}
