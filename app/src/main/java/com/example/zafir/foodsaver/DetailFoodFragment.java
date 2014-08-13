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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafir.foodsaver.data.RestaurantContract;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        //handle the saved button being clicked
        saveButtonHandler(rootView);
        //handle rating bar changes
        ratingBarHandler(rootView);

        return rootView;
    }

    private void ratingBarHandler(View rootView) {
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.detail_food_ratingbar);
        int curr = (int) ratingBar.getRating();
        Toast.makeText(getActivity(), String.valueOf(curr), Toast.LENGTH_SHORT).show();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int curr = (int) ratingBar.getRating();
                Toast.makeText(getActivity(), String.valueOf(curr) + " stars!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveButtonHandler(final View rootView) {
        Button saveButton = (Button) rootView.findViewById(R.id.detail_food_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String successMessage = "Success!";
                Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();

                //get today's date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String todaysDate = sdf.format(new Date());
                //get restaurant name
                String name = parts[0].trim();
                //get restaurant address
                String address = parts[1].trim();
                //get item
                EditText itemText = (EditText) getView().findViewById(R.id.itemText);
                String item = itemText.getText().toString();
                //get rating
                RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.detail_food_ratingbar);
                int rating = (int) ratingBar.getRating();
                //get restaurant description
                EditText descText = (EditText) getView().findViewById(R.id.descriptionText);
                String description = descText.getText().toString();

                Log.v(LOG_TAG, "Name is: " + name + " address is: " + address + " " +
                        "description is: " + description);
                Log.v(LOG_TAG, "Inserting data into the database!");
                if (descText == null || itemText == null) {
                    Toast.makeText(getActivity(), "it's null!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT).show();
                }


                ContentValues myRestaurantEntry = new ContentValues();
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_DATE, todaysDate);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY, name);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS, address);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_ITEM, item);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_RATING, rating);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_DESC, description);
                Uri restaurantInsertUri = getActivity().getContentResolver()
                        .insert(RestaurantContract.RestaurantEntry.CONTENT_URI, myRestaurantEntry);
                Log.v(LOG_TAG, "RESATAURANT URI IS " + restaurantInsertUri);
            }
        });
    }
}