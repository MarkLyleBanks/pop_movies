package com.marklylebanks.popularmovies;


public class Movie {
    private String mId;
    private String mTitle;
    private String mPhoto;
    private String mReleaseDate;
    private String mViewerRating;
    private String mOverview;

    public Movie(String id, String title, String photo, String date, String rating, String overview) {
        mId = id;
        mTitle = title;
        mPhoto = photo;
        mReleaseDate = date;
        mViewerRating = rating;
        mOverview = overview;
    }

    public String getId() { return mId; }

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
