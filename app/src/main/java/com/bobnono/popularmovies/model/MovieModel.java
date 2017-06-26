package com.bobnono.popularmovies.model;

import com.bobnono.popularmovies.data.MoviePreferences;
import com.bobnono.popularmovies.utilties.DateUtils;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by user on 2017-06-25.
 */

public class MovieModel implements Serializable {
    private int id;
    private String title;
    private String poster_path;
    private Double vote_average;
    private String overview;
    private Calendar release_date;

    public static final String MDBDateFormat = "yyyy-MM-dd";
    public static final String MDBMaximumRating = "10";

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
}
