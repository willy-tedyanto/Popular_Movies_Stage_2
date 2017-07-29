package com.bobnono.popularmovies.model;

/**
 * Created by user on 2017-07-28.
 */

public class TrailerModel {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public TrailerModel(){}

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }

    public void setIso_639_1(String iso_639_1){ this.iso_639_1 = iso_639_1; };
    public String getIso_639_1() {
        return this.iso_639_1;
    }

    public void setIso_3166_1(String iso_639_1){ this.iso_639_1 = iso_639_1; }
    public String getIso_3166_1(){
        return this.iso_3166_1;
    }

    public void setKey(String key){ this.key = key; }
    public String getKey(){
        return this.key;
    }

    public void setName(String name){ this.name = name;}
    public String getName(){
        return this.name;
    }

    public void setSite(String site){ this.site = site; }
    public String getSite(){
        return this.site;
    }

    public void setSize(int size){ this.size = size; }
    public int getSize(){
        return this.size;
    }

    public void setType(String type){ this.type = type; }
    public String getType(){
        return this.type;
    }

}
