package com.bobnono.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bobnono.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by user on 2017-07-28.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    String TAG = "MovieDbHelper";

    private static final String DATABASE_NAME = "moviesDb.db";

    private static final int VERSION = 7;

    public MovieDbHelper(Context context){

        super(context, DATABASE_NAME, null, VERSION);
        Log.e(TAG, "Version DB : " + VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID                      + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_MOVIE_ID          + " INTEGER UNIQUE, " +
                MovieEntry.COLUMN_TITLE             + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE      + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE      + " REAL NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW          + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH       + " TEXT NOT NULL);";

        Log.e(TAG, "Enter Oncreate DB : " + CREATE_TABLE);

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

        Log.e(TAG, "Enter Drop Table : " + sql);
        db.execSQL(sql);

        onCreate(db);
    }
}
