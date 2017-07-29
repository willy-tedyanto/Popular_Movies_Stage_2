package com.bobnono.popularmovies.model;

/**
 * Created by user on 2017-07-28.
 */

public class ReviewModel {
    private String id;
    private String author;
    private String content;
    private String url;

    public ReviewModel(){}

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }

    public void setAuthor(String author) { this.author = author; }
    public String getAuthor() { return this.author; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return this.content; }

    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }

}
