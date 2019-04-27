package ch.hslu.mobpro.watchlist.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ch.hslu.mobpro.watchlist.model.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    List<Movie> getAll();

    @Query("SELECT * FROM movie WHERE title LIKE :title LIMIT 1")
    Movie findByTitle(String title);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Query("SELECT * FROM movie WHERE watchlist = 1")
    List<Movie> getAllWatchlist();

    @Query("UPDATE movie SET watchlist = :value WHERE id = :id")
    void setWatchlist(int id, boolean value);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}