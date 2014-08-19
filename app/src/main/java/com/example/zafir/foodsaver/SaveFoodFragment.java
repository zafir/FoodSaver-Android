package com.example.zafir.foodsaver;

/**
 * Created by zafir on 8/6/14.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Fragment used by SaveFoodActivity. Displays nearby restaurants, and allows the user to
 * search for restaurants if the one they are looking for isn't found in the list. Finally, allows
 * the user to manually input a restaurant in case neither of those options work.
 */
public class SaveFoodFragment extends Fragment implements LocationListener {
    // Used to get user's current location
    private LocationManager locationManager;
    private String provider;

    // Latitude and longitutde are passed to the Google Places API to either display nearby restaurants
    // or search for one
    private double lat = Double.MIN_VALUE;
    private double lon = Double.MIN_VALUE;
    private final String LOG_TAG = SaveFoodFragment.class.getSimpleName();

    // Custom array adapter used to take the restaurant data and display it in the UI
    private CustomArrayAdapter mRestaurantAdapter;

    // Query listener object is part of the Action Bar search widget. Detects when a query is submitted.
    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        // Required method, not used in our case
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

        // Calls the searchRestaurants() method when a query is submitted
        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
            searchRestaurants(query);
            return false;
        }
    };

    public SaveFoodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Allows the system to detect the options menu
        setHasOptionsMenu(true);

        // Identifies location immediately - since this can take time and is needed for the rest
        // of the fragment and activity
        getLocation();
    }

    /**
     * Helper method to to identify the user's location
     */
    private void getLocation() {
        Toast.makeText(getActivity(), "Finding location...", Toast.LENGTH_SHORT).show();

        // Boilerplate code to retrieve user location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(getActivity(), "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param menu
     * @param inflater
     * Creates the Search widget in the action bar
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_food, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(queryListener);
    }

    /**
     *
     * @param item
     * @return
     * Allows the user to refresh results by re-calibrating their location and updating the nearby
     * restaurants list
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getLocation();
            updateRestaurants();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param query is the search query that the user entered in the search widget
     * Performs search via the Google Places API in an asynchronous task. Uses an async task
     * to remove the work from the main UI thread
     */
    private void searchRestaurants(String query) {
        // Initializes the async task
        SearchNearbyRestaurantsTask restaurantsTask = new SearchNearbyRestaurantsTask(getActivity(),
                mRestaurantAdapter);
        //if (lat == Double.MIN_VALUE) {
        //    lat = null;
        //    lon = -122.080521;
        //}

        // Executes the task
        restaurantsTask.execute(String.valueOf(lat), String.valueOf(lon), query);
    }

    /**
     * Retrieves and displays restaurants near the user's current location
     */
    private void updateRestaurants() {
        // Initializes the async task
        FetchNearbyRestaurantsTask restaurantsTask = new FetchNearbyRestaurantsTask(getActivity(),
                mRestaurantAdapter);
        //if (lat == Double.MIN_VALUE) {
        //    lat = null;
        //    lon = -122.080521;
        //}

        // Executes the task
        restaurantsTask.execute(String.valueOf(lat), String.valueOf(lon));
    }

    /**
     * Method used in location retrieval. After getting location, uses this information to do something.
     * In this case, finds and displays nearby restaurants
     */
    @Override
    public void onStart() {
        super.onStart();
        updateRestaurants();
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * Initializes the adapter which serves as the bridge between the data retrieved from the API
     * and the UI. Attaches it to the appropriate ListView.
     * Creates click listener to launch a new activity upon a list item tap
     * Creates button to input a restaurant manually if it is not found in the list
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_save_food, container, false);

        // Initalizes adapter
        mRestaurantAdapter = new CustomArrayAdapter(
                getActivity(),
                R.layout.list_item_nearby_restaurants,
                R.id.list_item_nearby_restaurants_textview,
                new ArrayList<String>()
        );

        // Attaches it to the ListView
        ListView listView = (ListView) rootView.findViewById(R.id.listview_restaurant);
        listView.setAdapter(mRestaurantAdapter);

        //getActivity().setProgressBarIndeterminateVisibility(false);

        // Handles list item click, which launches a new activity. Passes the name of the restaurant
        // to the next activity as an intent extra
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String restaurant = mRestaurantAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailFoodActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, restaurant);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Creates button that allows user to tap and enter information if they can't find their
        // restaurant displayed
        Button notFound = (Button) rootView.findViewById(R.id.button_cant_find_restaurant);
        notFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(inflater, rootView);
            }
        });

        return rootView;
    }

    /**
     *
     * @param inflater
     * @param rootView
     * Creates the form in the dialog to manually input restaurant information if it is not
     * found in the list
     */
    private void showDialog(final LayoutInflater inflater, final View rootView) {
        // Builds the dialog skeleton
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
        .setView(inflater.inflate(R.layout.dialog_not_found, null))
        .setPositiveButton(R.string.save_entry, null)
        .setNegativeButton(R.string.cancel, null)
        .setTitle(R.string.restaurant_not_found_button)
        .create();

        // Sets click listeners for the positive and negative buttons on the dialogue.
        // The users submission is passed as an intent extra
        // If the user chooses to submit empty data, we populate it with the word "empty"
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Finds the fields for name and address
                        EditText name = (EditText) rootView.findViewById(R.id.dialog_name);
                        EditText address = (EditText) rootView.findViewById(R.id.dialog_address);
                        String nameInput;
                        String addressInput;

                        // Sets the strings which are passed as intent extras to either default "empty"
                        // or what the user entered
                        if (name != null) {
                            nameInput = String.valueOf(name.getText());
                        } else {
                            nameInput = "Empty";
                        }

                        if (address == null) {
                            addressInput = "";
                        } else {
                            addressInput = String.valueOf(address.getText());
                        }

                        String intentExtra = nameInput + " - " + addressInput;
                        Intent intent = new Intent(getActivity(), DetailFoodActivity.class)
                                .putExtra(Intent.EXTRA_TEXT, intentExtra);

                        // Starts the next activity
                        startActivity(intent);

                        // Finishes this activity to remove it from the activity stack
                        getActivity().finish();

                    }
                });
            }
        });

        builder.show();
    }

    /**
     * Resets the latitude and longitude which are used in the two API calls - search and nearby restaurants -
     * if the user's location changes
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        Log.v(LOG_TAG,"LAT SET: " + lat + " LON SET: " + lon);
        Toast.makeText(getActivity(), "Lat long set!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Required method for location, not used in our case
     * @param s
     * @param i
     * @param bundle
     */
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    /**
     * Required method for location, not used in our case
     * @param s
     */
    @Override
    public void onProviderEnabled(String s) {
    }

    /**
     * Required method for location, not used in our case
     * @param s
     */
    @Override
    public void onProviderDisabled(String s) {
    }


}