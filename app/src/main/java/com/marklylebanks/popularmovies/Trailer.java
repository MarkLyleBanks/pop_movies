package com.marklylebanks.popularmovies;

public class Trailer {
    private String mName;
    private String mKey;

    Trailer(String name, String key){
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
