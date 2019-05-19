package ch.hslu.mobpro.watchlist.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import java.util.List;
import ch.hslu.mobpro.watchlist.model.Movie;
import ch.hslu.mobpro.watchlist.repository.MovieRepository;
import ch.hslu.mobpro.watchlist.R;
import ch.hslu.mobpro.watchlist.adapter.MoviesAdapter;

public class WatchlistFragment extends Fragment {
    private MovieRepository movieRepository;
    private List<Movie> movies;

    public static WatchlistFragment newInstance() {
        return new WatchlistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        movieRepository = new MovieRepository(getContext());
        View view = inflater.inflate(R.layout.watchlist_view, container, false);

        RecyclerView rvMovies = view.findViewById(R.id.rvMovies);
        movies = movieRepository.getAllWatchlist();

        MoviesAdapter adapter = new MoviesAdapter(movies, new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie item) {
                /**
                 *  OPENS DETAIL FRAGMENT
                 */
                Gson gson = new Gson();
                String jsonString = gson.toJson(item);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, DetailFragment.newInstance(jsonString, true)).commit();
            }
        });
        rvMovies.setAdapter(adapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
