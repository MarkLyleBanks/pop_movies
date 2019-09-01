package com.marklylebanks.popularmovies;

public class Trailers {
    private String mName;
    private String mKey;

    Trailers(String name, String key){
        mName = name;
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }
}
