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
 * A placeholder fragment containing a simple view.
 */
public class SaveFoodFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    private double lat = Double.MIN_VALUE;
    private double lon = Double.MIN_VALUE;
    private final String LOG_TAG = SaveFoodFragment.class.getSimpleName();
    private CustomArrayAdapter mRestaurantAdapter;

    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

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
        setHasOptionsMenu(true);
        getLocation();
    }

    private void getLocation() {
        Toast.makeText(getActivity(), "Finding location...", Toast.LENGTH_SHORT).show();
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

    private void searchRestaurants(String query) {
        SearchNearbyRestaurantsTask restaurantsTask = new SearchNearbyRestaurantsTask(getActivity(),
                mRestaurantAdapter);
        /*if (lat == Double.MIN_VALUE) {
            lat = null;
            lon = -122.080521;
        }*/
        restaurantsTask.execute(String.valueOf(lat), String.valueOf(lon), query);
    }

    private void updateRestaurants() {
        FetchNearbyRestaurantsTask restaurantsTask = new FetchNearbyRestaurantsTask(getActivity(),
                mRestaurantAdapter);
        /*if (lat == Double.MIN_VALUE) {
            lat = null;
            lon = -122.080521;
        }*/
        restaurantsTask.execute(String.valueOf(lat), String.valueOf(lon));
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRestaurants();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_save_food, container, false);

        mRestaurantAdapter = new CustomArrayAdapter(
                getActivity(),
                R.layout.list_item_nearby_restaurants,
                R.id.list_item_nearby_restaurants_textview,
                new ArrayList<String>()
        );
        ListView listView = (ListView) rootView.findViewById(R.id.listview_restaurant);
        listView.setAdapter(mRestaurantAdapter);

        getActivity().setProgressBarIndeterminateVisibility(false);
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

        //check for button click
        Button notFound = (Button) rootView.findViewById(R.id.button_cant_find_restaurant);
        notFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(inflater, rootView);
            }
        });

        return rootView;
    }

    private void showDialog(final LayoutInflater inflater, final View rootView) {
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
        .setView(inflater.inflate(R.layout.dialog_not_found, null))
        .setPositiveButton(R.string.save_entry, null)
        .setNegativeButton(R.string.cancel, null)
        .setTitle(R.string.restaurant_not_found_button)
        .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText name = (EditText) rootView.findViewById(R.id.dialog_name);
                        EditText address = (EditText) rootView.findViewById(R.id.dialog_address);
                        String nameInput;
                        String addressInput;
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
                        startActivity(intent);
                        getActivity().finish();

                    }
                });
            }
        });

        builder.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        Log.v(LOG_TAG,"LAT SET: " + lat + " LON SET: " + lon);
        Toast.makeText(getActivity(), "Lat long set!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //auto generated - do nothing
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


}