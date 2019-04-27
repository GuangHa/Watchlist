package ch.hslu.mobpro.watchlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.hslu.mobpro.watchlist.model.Movie;
import ch.hslu.mobpro.watchlist.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Movie item);
    }

    private List<Movie> mMovies;
    private OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView genreTextView;
        public TextView runtimeTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            nameTextView = itemView.findViewById(R.id.movie_name);
            genreTextView = itemView.findViewById(R.id.movie_genre);
            runtimeTextView = itemView.findViewById(R.id.movie_runtime);
        }

        public void bind(final Movie item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public MoviesAdapter(List<Movie> movies, OnItemClickListener listener) {
        mMovies = movies;
        this.listener = listener;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_movie, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Movie movie = mMovies.get(position);

        // Set item views based on your views and data model
        TextView nameTV = viewHolder.nameTextView;
        nameTV.setText(movie.getTitle());

        TextView genreTV = viewHolder.genreTextView;
        genreTV.setText(movie.getGenre());

        TextView runTimeTV = viewHolder.runtimeTextView;
        runTimeTV.setText(movie.getRuntime());
        viewHolder.bind(mMovies.get(position), listener);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}
