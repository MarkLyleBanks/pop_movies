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
    public Movie(String movieId, String title, String photo, String releaseDate, String viewerRating, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.photo = photo;
        this.releaseDate = releaseDate ;
        this.viewerRating = viewerRating;
        this.overview = overview;
    }

    public Movie(int id, String movieId, String title, String photo, String releaseDate, String viewerRating, String overview) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.photo = photo;
        this.releaseDate = releaseDate ;
        this.viewerRating = viewerRating;
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getViewerRating() {
        return viewerRating;
    }

    public void setViewerRating(String viewerRating) {
        this.viewerRating = viewerRating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
