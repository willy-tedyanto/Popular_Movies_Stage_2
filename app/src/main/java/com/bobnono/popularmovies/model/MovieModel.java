package com.bobnono.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bobnono.popularmovies.data.MoviePreferences;
import com.bobnono.popularmovies.utilities.DateUtils;

import java.util.Calendar;

/**
 * Created by user on 2017-06-25.
 */

public class MovieModel implements Parcelable {
    private int id;
    private String title;
    private String poster_path;
    private Double vote_average;
    private String overview;
    private Calendar release_date;

    public static final String MDBDateFormat = "yyyy-MM-dd";
    public static final String MDBMaximumRating = "10";

    // No-arg Ctor
    public MovieModel(){}

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setPosterPath(String poster_path){
        this.poster_path = poster_path;
    }

    public String getPosterPath(){
        return MoviePreferences.getURLPoster(this.poster_path);
    }

    public void setVoteAverage(Double vote_average) {this.vote_average = vote_average; }
    public Double getVoteAverage() {return this.vote_average; }

    public void setOverview(String overview) {this.overview = overview; }
    public String getOverview() {return this.overview; }

    public void setReleaseDate(String release_date) {
        this.release_date = DateUtils.stringToCalendar(release_date, MDBDateFormat);
    }

    public Calendar getReleaseDate() {return this.release_date; }

    public String getRating(){
        return Double.toString(getVoteAverage()) + "/" + MDBMaximumRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeDouble(vote_average);
        dest.writeString(overview);
        dest.writeString(DateUtils.calendarToString(release_date, MDBDateFormat));

    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {
        public MovieModel createFromParcel(Parcel pc) {
            return new MovieModel(pc);
        }
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public MovieModel(Parcel in){
        id = in.readInt();
        title = in.readString();
        poster_path = in.readString();
        vote_average = in.readDouble();
        overview = in.readString();
        release_date = DateUtils.stringToCalendar(in.readString(), MDBDateFormat);
    }
}
