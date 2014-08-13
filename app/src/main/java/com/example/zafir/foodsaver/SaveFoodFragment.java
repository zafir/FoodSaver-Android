package com.example.zafir.foodsaver;

/**
 * Created by zafir on 8/6/14.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    private ArrayAdapter<String> mRestaurantAdapter;

    public SaveFoodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateRestaurants();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateRestaurants() {
        FetchNearbyRestaurantsTask restaurantsTask = new FetchNearbyRestaurantsTask(getActivity(),
                mRestaurantAdapter);
        if (lat == Double.MIN_VALUE) {
            lat = 37.397488;
            lon = -122.080521;
        }
        restaurantsTask.execute(String.valueOf(lat), String.valueOf(lon));
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRestaurants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save_food, container, false);

        mRestaurantAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_nearby_restaurants,
                R.id.list_item_nearby_restaurants_textview,
                new ArrayList<String>()
        );
        ListView listView = (ListView) rootView.findViewById(R.id.listview_restaurant);
        listView.setAdapter(mRestaurantAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String restaurant = mRestaurantAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailFoodActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, restaurant);
                startActivity(intent);
            }
        });

        //check for button click
        Button notFound = (Button) rootView.findViewById(R.id.button_cant_find_restaurant);
        notFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("foo").setTitle(R.string.restaurant_not_found_button);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position%2==0) {
            convertView.setBackgroundColor(R.color.light_gray);
        }
        return view;
    }*/


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