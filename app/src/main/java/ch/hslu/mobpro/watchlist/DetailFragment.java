package ch.hslu.mobpro.watchlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends Fragment {
    private MovieRepository movieRepository;
    private Movie movie;
    private Movie dbMovie;

    public static DetailFragment newInstance(String string) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("jsonObjectString", string);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String jsonObjectString = getArguments().getString("jsonObjectString");
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);
            setUpMovie(jsonObject);
        } catch (JSONException exception) {
            Log.e("Detail onCreateView: ", exception.getMessage());
        }
        View view = inflater.inflate(R.layout.detail_view, container, false);
        Button btn = view.findViewById(R.id.addAndRemoveFromListButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set isWatchlist to TRUE if its false and reverse
                dbMovie = movieRepository.getMovieByTitle(dbMovie.getTitle());
                if(dbMovie != null && dbMovie.getWatchlist() == false) {
                    movieRepository.setWatchlist(dbMovie, true);
                    Button addAndRemoveFromListButton = getView().findViewById(R.id.addAndRemoveFromListButton);
                    addAndRemoveFromListButton.setText("REMOVE FROM WATCHLIST");
                    Toast.makeText(getContext(), "Added to the list!", Toast.LENGTH_LONG).show();
                } else {
                    movieRepository.setWatchlist(dbMovie, false);
                    Button addAndRemoveFromListButton = getView().findViewById(R.id.addAndRemoveFromListButton);
                    addAndRemoveFromListButton.setText("ADD TO WATCHLIST");
                    Toast.makeText(getContext(), "Removed from the list!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = getView().findViewById(R.id.imageView);
        Picasso.get().load(movie.getPoster()).into(imageView);

        TextView titleTextView = getView().findViewById(R.id.movieTitleTextView);
        titleTextView.setText(movie.getTitle());

        TextView subtitleTextView = getView().findViewById(R.id.movieSubtitleTextView);
        subtitleTextView.setText(movie.getYear() + " - " + movie.getGenre() + " - " + movie.getRuntime());

        TextView actorsTextView = getView().findViewById(R.id.actorsTextView);
        actorsTextView.setText(movie.getActors());

        TextView descriptionTextView = getView().findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(movie.getPlot());

        dbMovie = movieRepository.getMovieByTitle(movie.getTitle());
        if(dbMovie.getWatchlist() != null && dbMovie.getWatchlist() == true) {
            Button addAndRemoveFromListButton = getView().findViewById(R.id.addAndRemoveFromListButton);
            addAndRemoveFromListButton.setText("REMOVE FROM WATCHLIST");
        }

        // TODO: display more infos?
        // TODO: change layout to scrollview?
    }

    private void setUpMovie(JSONObject jsonObject) {
        movieRepository = new MovieRepository(getContext());
        setMovie(jsonObject);
        // INSERT MOVIE INTO DB IF ITS NOT ALREADY IN
        dbMovie = movieRepository.getMovieByTitle(movie.getTitle());
        if(dbMovie == null) {
            movieRepository.insertMovie(movie);
        }
    }

    private void setMovie(JSONObject jsonObject) {
        movie = new Movie();
        try {
            if (jsonObject.has("Title")) {
                movie.setTitle(jsonObject.getString("Title"));
            }
            if (jsonObject.has("Year")) {
                movie.setYear(jsonObject.getString("Year"));
            }
            if (jsonObject.has("Rated")) {
                movie.setRated(jsonObject.getString("Rated"));
            }
            if (jsonObject.has("Released")) {
                movie.setReleased(jsonObject.getString("Released"));
            }
            if (jsonObject.has("Runtime")) {
                movie.setRuntime(jsonObject.getString("Runtime"));
            }
            if (jsonObject.has("Genre")) {
                movie.setGenre(jsonObject.getString("Genre"));
            }
            if (jsonObject.has("Director")) {
                movie.setDirector(jsonObject.getString("Director"));
            }
            if (jsonObject.has("Writer")) {
                movie.setWriter(jsonObject.getString("Writer"));
            }
            if (jsonObject.has("Actors")) {
                movie.setActors(jsonObject.getString("Actors"));
            }
            if (jsonObject.has("Plot")) {
                movie.setPlot(jsonObject.getString("Plot"));
            }
            if (jsonObject.has("Language")) {
                movie.setLanguage(jsonObject.getString("Language"));
            }
            if (jsonObject.has("Country")) {
                movie.setCountry(jsonObject.getString("Country"));
            }
            if (jsonObject.has("Awards")) {
                movie.setAwards(jsonObject.getString("Awards"));
            }
            if (jsonObject.has("Poster")) {
                movie.setPoster(jsonObject.getString("Poster"));
            }
            if (jsonObject.has("Metascore")) {
                movie.setMetascore(jsonObject.getString("Metascore"));
            }
            if (jsonObject.has("imdbRating")) {
                movie.setImdbRating(jsonObject.getString("imdbRating"));
            }
            if (jsonObject.has("imdbVotes")) {
                movie.setImdbVotes(jsonObject.getString("imdbVotes"));
            }
            if (jsonObject.has("Type")) {
                movie.setType(jsonObject.getString("Type"));
            }
            if (jsonObject.has("DVD")) {
                movie.setDvd(jsonObject.getString("DVD"));
            }
            if (jsonObject.has("BoxOffice")) {
                movie.setBoxOffice(jsonObject.getString("BoxOffice"));
            }
            if (jsonObject.has("Production")) {
                movie.setProduction(jsonObject.getString("Production"));
            }
            if (jsonObject.has("Website")) {
                movie.setWebsite(jsonObject.getString("Website"));
            }
            if (jsonObject.has("totalSeasons")) {
                movie.setTotalSeasons(jsonObject.getString("totalSeasons"));
            }
            movie.setWatchlist(false);
        } catch (JSONException exception) {
            Log.e("Detail setMovie: ", exception.getMessage());
        }
    }
}
