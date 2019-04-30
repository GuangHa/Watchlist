package ch.hslu.mobpro.watchlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import ch.hslu.mobpro.watchlist.fragment.DetailFragment;
import ch.hslu.mobpro.watchlist.fragment.HomeFragment;
import ch.hslu.mobpro.watchlist.fragment.MasterFragment;
import ch.hslu.mobpro.watchlist.fragment.SearchFragment;
import ch.hslu.mobpro.watchlist.fragment.SettingsFragment;
import ch.hslu.mobpro.watchlist.fragment.WatchlistFragment;

public class MainActivity extends AppCompatActivity implements MasterFragment.Callbacks {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;

    private static final String TAG_MASTER_FRAGMENT = "TAG_MASTER_FRAGMENT";
    private static final String TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, homeFragment).commit();
        } else {
            fragmentManager = getSupportFragmentManager();
            MasterFragment masterFragment = MasterFragment.newInstance();
            HomeFragment homeFragment = HomeFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.master_fragment_container, masterFragment, TAG_MASTER_FRAGMENT).commit();
            fragmentManager.beginTransaction().add(R.id.detail_fragment_container, homeFragment, TAG_HOME_FRAGMENT).commit();
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
        String url = "http://www.omdbapi.com/?apikey=49284ee8&plot=short&r=json&t="+editText.getText().toString();
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
}