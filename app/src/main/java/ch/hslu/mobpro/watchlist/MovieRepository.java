package ch.hslu.mobpro.watchlist;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.VolleyError;

import java.util.List;

public class MovieRepository {
    private MovieDao movieDao;
    private List<Movie> allMovies;

    private AppDatabase movieDatabase;
    public MovieRepository(Context context) {
        movieDatabase = AppDatabase.getDatabase(context);
        movieDao = movieDatabase.movieDao();
        allMovies = movieDao.getAll();
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
