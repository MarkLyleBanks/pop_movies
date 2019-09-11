package com.marklylebanks.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorite_movies")
    LiveData<List<Movie>> loadAllTasks();

    @Query("SELECT EXISTS(SELECT * FROM favorite_movies WHERE movie_id = :id)")
            int checkExists(String id);

    @Query("DELETE FROM favorite_movies WHERE movie_id = :id")
            void deleteFromFavorites(String id);

    @Insert void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete void deleteMovie(Movie movie);
}
