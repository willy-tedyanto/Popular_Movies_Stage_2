package com.bobnono.popularmovies.utilties;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.bobnono.popularmovies.R;

/**
 * Created by user on 2017-06-27.
 */

public class GeneralUtils {

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


}
