package com.marklylebanks.popularmovies;

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
    List<Movie> loadAllTasks();

    @Insert void insertTask(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete void deleteMovie(Movie movie);
}
