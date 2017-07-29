package com.bobnono.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bobnono.popularmovies.R;
import com.bobnono.popularmovies.data.MovieContract;
import com.bobnono.popularmovies.model.MovieModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by user on 2017-06-27.
 */

public class GeneralUtils {
    static String TAG = "GeneralUtils";

    public static int calculateNoOfColumns(Context context, int scalingFactor) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    public static String readSharedPreferences(Context context, String key, String defaultValue){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        return sharedPref.getString(key, defaultValue);

    }

    public static int readSharedPreferences(Context context, String key, int defaultValue){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        return sharedPref.getInt(key, defaultValue);

    }

    public static void writeSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public static void writeSharedPreference(Context context, String key, int value){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveImageFromImageView(Context context, ImageView imageView, String fileName) {

        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        File dir = new File(MoviePreferences.POSTER_LOCAL_STORAGE_DIR
                + "/" + context.getString(R.string.favorite_movie_image_folder));

        dir.mkdir();

        String fname = fileName + context.getString(R.string.image_poster_file_extension);
        File file = new File (dir, fname);

        if (file.exists ()) {
            file.delete ();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteImageFavorite(Context context, String fileName){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File dir = new File(root + "/" + context.getString(R.string.favorite_movie_image_folder));
        String fname = fileName + ".jpg";
        File file = new File (dir, fname);

        if (file.exists()) {
            file.delete();
        }
    }

    public static Boolean recordExist(Context context, int movieId){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;

        Cursor resCursor = context.getContentResolver().query(uri,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Integer.toString(movieId)},
                null);

        if (resCursor.getCount() > 0){
            resCursor.close();
            return true;
        } else {
            resCursor.close();
            return false;
        }
    }

    public static ArrayList<MovieModel> cursorToArrayListMovieModel(Cursor cursor){
        ArrayList<MovieModel> moviesList = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                MovieModel movie = new MovieModel();

                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE))));
                movie.setIsFavorite(true);

                moviesList.add(movie);

            }
        } finally {
            cursor.close();
        }
        return moviesList;
    }

}
