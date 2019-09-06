package com.marklylebanks.popularmovies;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private String movieId;
    private String title;
    private String photo;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "viewer_rating")
    private String viewerRating;
    private String overview;



    @Ignore
    public Movie(String movieId, String title, String photo, String date, String rating, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.photo = photo;
        releaseDate = date;
        viewerRating = rating;
        this.overview = overview;
    }

    public Movie(int id, String movieId, String title, String photo, String date, String rating, String overview) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.photo = photo;
        releaseDate = date;
        viewerRating = rating;
        this.overview = overview;
    }

    public int getId(){
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setViewerRating(String viewerRating) {
        this.viewerRating = viewerRating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
