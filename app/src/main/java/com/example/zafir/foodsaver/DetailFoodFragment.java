package com.example.zafir.foodsaver;

/**
 * Created by zafir on 8/8/14.
 */

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafir.foodsaver.data.RestaurantContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFoodFragment extends Fragment {
    String[] parts;
    private final String LOG_TAG = DetailFoodFragment.class.getSimpleName();

    public DetailFoodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_food, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String restaurantStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            parts = restaurantStr.split("-");
            ((TextView) rootView.findViewById(R.id.detail_food))
                    .setText(restaurantStr);
        }

        Button saveButton = (Button) rootView.findViewById(R.id.detail_food_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String successMessage = "Success!";
                Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();
                String name = parts[0].trim();
                String address = parts[1].trim();
                EditText descText = (EditText) getView().findViewById(R.id.descriptionText);
                String description = descText.getText().toString();
                Log.v(LOG_TAG, "Name is: " + name + " address is: " + address + " " +
                        "description is: " + description);
                Log.v(LOG_TAG, "Inserting data into the database!");

                ContentValues myRestaurantEntry = new ContentValues();
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY, name);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS, address);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_DESC, description);
                Uri restaurantInsertUri = getActivity().getContentResolver()
                        .insert(RestaurantContract.RestaurantEntry.CONTENT_URI, myRestaurantEntry);
                Log.v(LOG_TAG, "RESATAURANT URI IS " + restaurantInsertUri);
            }
        });

        return rootView;
    }
}