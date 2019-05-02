package ch.hslu.mobpro.watchlist.repository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ch.hslu.mobpro.watchlist.interfaces.MovieDao;
import ch.hslu.mobpro.watchlist.database.AppDatabase;
import ch.hslu.mobpro.watchlist.model.Movie;

public class MovieRepository {
    private MovieDao movieDao;
    private List<Movie> allMovies;

    private AppDatabase movieDatabase;
    public MovieRepository(Context context) {
        movieDatabase = AppDatabase.getDatabase(context);
        movieDao = movieDatabase.movieDao();
        allMovies = movieDao.getAll();
    }

    public List<Movie> getAllWatchlist() {
        return movieDao.getAllWatchlist();
    }

    public List<Movie> getAllCinemaMoviesByYear(String year) {
        return movieDao.getAllCinemaMoviesByYear(year);
    }

    public Movie getRandomCinemaMovie(String year) {
        return movieDao.getRandomCinemaMovie(year);
    }

    public void insertMovie(Movie movie) {
        new insertAsyncTask(movieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }

    public void setWatchlist(Movie movie, Boolean isWatchlist) {
        MyTaskParams params = new MyTaskParams(movie, isWatchlist);
        new updateAsyncTask(movieDao).execute(params);
    }

    private static class updateAsyncTask extends AsyncTask<MyTaskParams, Void, Void> {
        private MovieDao mAsyncTaskDao;

        updateAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MyTaskParams... params) {
            mAsyncTaskDao.setWatchlist(params[0].movie.getId(), params[0].isWatchlist);
            return null;
        }
    }

    private static class MyTaskParams {
        Movie movie;
        Boolean isWatchlist;

        MyTaskParams(Movie movie, Boolean isWatchlist) {
            this.movie = movie;
            this.isWatchlist = isWatchlist;
        }
    }

    /*public void deleteMovie(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                movieDatabase.movieDao().deleteMovie(movie);
                return null;
            }
        }.execute();
    }*/

    public Movie getMovieByTitle(String title) {
        return movieDatabase.movieDao().findByTitle(title);
    }

    public List<Movie> getAll() {
        return allMovies;
    }
}
