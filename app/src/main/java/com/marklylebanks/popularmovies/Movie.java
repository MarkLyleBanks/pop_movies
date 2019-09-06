package com.marklylebanks.popularmovies;


public class Movie {
    private String movieId;
    private String title;
    private String photo;
    private String releaseDate;
    private String viewerRating;
    private String overview;

    public Movie(String movieId, String title, String photo, String date, String rating, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.photo = photo;
        releaseDate = date;
        viewerRating = rating;
        this.overview = overview;
    }

    public String getMovieId() { return movieId; }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getViewerRating() {
        return viewerRating;
    }

    public String getOverview() {
        return overview;
    }

}
