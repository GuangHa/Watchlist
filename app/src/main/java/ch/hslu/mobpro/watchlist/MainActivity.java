package ch.hslu.mobpro.watchlist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.hslu.mobpro.watchlist.fragment.DetailFragment;
import ch.hslu.mobpro.watchlist.fragment.HomeFragment;
import ch.hslu.mobpro.watchlist.fragment.MasterFragment;
import ch.hslu.mobpro.watchlist.fragment.SearchFragment;
import ch.hslu.mobpro.watchlist.fragment.SettingsFragment;
import ch.hslu.mobpro.watchlist.fragment.WatchlistFragment;
import ch.hslu.mobpro.watchlist.model.Movie;
import ch.hslu.mobpro.watchlist.repository.MovieRepository;

public class MainActivity extends AppCompatActivity implements MasterFragment.Callbacks {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private MovieRepository movieRepository;
    private String jsonString;
    static int notificationCount = 0;

    private static final String TAG_MASTER_FRAGMENT = "TAG_MASTER_FRAGMENT";
    private static final String TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String movieTitle = getIntent().getStringExtra("MovieTitle");
        movieRepository = new MovieRepository(getApplicationContext());
        if(movieTitle != null) {
            Movie item = movieRepository.getMovieByTitle(movieTitle);
            Gson gson = new Gson();
            jsonString = gson.toJson(item);
        } else {
            insertCinemaMovies();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout != null) {
            navigationView = (NavigationView) findViewById(R.id.master_fragment_container);
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    selectDrawerItem(item);
                    return true;
                }
            });

            final ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = HomeFragment.newInstance();
            if(movieTitle != null) {
                fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, DetailFragment.newInstance(jsonString, true)).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, homeFragment).commit();
            }
        } else {
            fragmentManager = getSupportFragmentManager();
            MasterFragment masterFragment = MasterFragment.newInstance();
            HomeFragment homeFragment = HomeFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.master_fragment_container, masterFragment, TAG_MASTER_FRAGMENT).commit();
            if(movieTitle != null) {
                fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, DetailFragment.newInstance(jsonString, true)).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.detail_fragment_container, homeFragment, TAG_HOME_FRAGMENT).commit();
            }
        }

        if(movieTitle == null) {
            android.support.v7.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            SharedPreferences sharedPref = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
            Boolean switchPref = sharedPref.getBoolean("recommendation_switch", false);
            if (switchPref) {
                createPushNotification();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        WatchlistFragment watchlistFragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId())
        {
            case R.id.home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.search:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.watchlist:
                watchlistFragment = new WatchlistFragment();
                fragmentClass = WatchlistFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(watchlistFragment != null) {
            fragment = watchlistFragment;
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        menuItem.setChecked(true);

        // Set action bar title
        setTitle(menuItem.getTitle());

        drawerLayout.closeDrawers();
    }

    /**
     * OnClick of searchButton
     * @param view
     */
    public void search(View view) {
        final EditText editText = findViewById(R.id.searchEditText);
        RequestQueue queue = Volley.newRequestQueue(this);
        // TODO: INPUT YOUR API KEY HERE
        String url = "http://www.omdbapi.com/?apikey=YOURAPIKEY&plot=short&r=json&t="+editText.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                /**
                 * OPENS DETAIL FRAGMENT
                 */
                if(response.has("Error")) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("Error"), Toast.LENGTH_LONG).show();
                    } catch(JSONException exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, DetailFragment.newInstance(response.toString(), false)).commit();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);

        /**
         * CLOSE KEYBOARD AFTER SEARCH
         */
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onMasterItemClicked(int masterItemId) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(masterItemId) {
            case 2:
                fragmentClass = SearchFragment.class;
                break;
            case 3:
                fragmentClass = WatchlistFragment.class;
                break;
            case 4:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, fragment).commit();

    }

    public void insertCinemaMovies() {
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("2019.txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            for(String line; (line = r.readLine()) != null; ) {
                JSONObject jsonObject = new JSONObject(line);
                setMovie(jsonObject);
            }
        } catch(IOException | JSONException exception) {
            Log.d("inserting cinema movies", exception.getMessage());
        }
    }

    private void setMovie(JSONObject jsonObject) {
        Movie movie = new Movie();
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
            // currently set every movie as non cinema movie
            movie.setIsCinema(true);
            Movie dbMovie = movieRepository.getMovieByTitle(movie.getTitle());
            if(dbMovie == null) {
                movieRepository.insertMovie(movie);
            }
        } catch (JSONException exception) {
            Log.e("Detail setMovie: ", exception.getMessage());
        }
    }

    private void createPushNotification() {
        createNotificationChannel();

        // GETS RANDOM MOVIE FROM DATABASE
        Movie randomMovie = movieRepository.getRandomCinemaMovie("2019");

        int requestID = (int) System.currentTimeMillis();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("MovieTitle", randomMovie.getTitle());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "some_channel_id")
                .setSmallIcon(R.drawable.ic_baseline_tv_24px) // TODO: CHANGE NOTIFICATION ICON
                .setContentTitle(randomMovie.getTitle())
                .setContentText(randomMovie.getGenre())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(randomMovie.getGenre()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationCount++, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Some Channel";
            String channelId = "some_channel_id";
            //String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}