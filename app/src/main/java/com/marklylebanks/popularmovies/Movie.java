package com.marklylebanks.popularmovies;


public class Movie{

    String mTitle;
    String mPhoto;
    String mReleaseDate;
    String mViewerRating;
    String mOverview;

    public Movie(String title, String photo, String date, String rating, String overview) {
        mTitle = title;
        mPhoto = photo;
        mReleaseDate = date;
        mViewerRating = rating;
        mOverview = overview;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getViewerRating() {
        return mViewerRating;
    }

    public String getOverview() {
        return mOverview;
    }


}
