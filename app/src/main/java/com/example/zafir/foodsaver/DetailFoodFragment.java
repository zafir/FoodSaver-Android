package com.example.zafir.foodsaver;

/**
 * Created by zafir on 8/8/14.
 */

import android.app.AlertDialog;
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
 * Fragment used by DetailFoodActivity. Displays fields for the user to fill in the name
 * of the restaurant, what they ate, a five star rating, and additional comments. Then, the user can save
 * the entry and store it in the backend.
 */
public class DetailFoodFragment extends Fragment {
    // Contains two strings, one representing the restaurant name and the other its address,
    // created by splitting the string passed via the intent which combines both pieces of information
    String[] parts;
    private final String LOG_TAG = DetailFoodFragment.class.getSimpleName();

    public DetailFoodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_food, container, false);
        Intent intent = getActivity().getIntent();

        // Splits the string passed in via the intent, which contains the restaurant name
        // and address separated by a dash.
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String restaurantStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            // Sets the restaurant name to the first part of the combined "name - address" restaurantStr
            // string
            parts = restaurantStr.split("-");
            ((TextView) rootView.findViewById(R.id.detail_food))
                    .setText(parts[0]);
        }

        // Handles the saved button being clicked
        saveButtonHandler(rootView);

        // Handles rating bar changes
        ratingBarHandler(rootView);

        return rootView;
    }

    /**
     *
     * @param rootView
     * Allows the user to change the rating on the RatingBar, which is a widget provided by
     * Android. Displays a toast letting the user know that their change was accepted.
     */
    private void ratingBarHandler(View rootView) {
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.detail_food_ratingbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int curr = (int) ratingBar.getRating();
                Toast.makeText(getActivity(), String.valueOf(curr) + " stars!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     *
     * @param rootView
     * Stores the entries into the database upon tapping save
     */
    private void saveButtonHandler(final View rootView) {
        Button saveButton = (Button) rootView.findViewById(R.id.detail_food_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get today's date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String todaysDate = sdf.format(new Date());

                // Get restaurant name
                String name = parts[0].trim();

                // Get restaurant address
                String address = parts[1].trim();

                // Get item
                EditText itemText = (EditText) getView().findViewById(R.id.itemText);
                String item = itemText.getText().toString();

                // Get rating
                RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.detail_food_ratingbar);
                int rating = (int) ratingBar.getRating();

                // Get restaurant description
                EditText descText = (EditText) getView().findViewById(R.id.descriptionText);
                String description = descText.getText().toString();

                // Logs entries into the database
                Log.v(LOG_TAG, "Name is: " + name + " address is: " + address + " " +
                        "description is: " + description);
                Log.v(LOG_TAG, "Inserting data into the database!");

                // Uses the content provider to insert the data into the database
                ContentValues myRestaurantEntry = new ContentValues();
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_DATE, todaysDate);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY, name);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS, address);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_ITEM, item);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_RATING, rating);
                myRestaurantEntry.put(RestaurantContract.RestaurantEntry.COLUMN_DESC, description);
                Uri restaurantInsertUri = getActivity().getContentResolver()
                        .insert(RestaurantContract.RestaurantEntry.CONTENT_URI, myRestaurantEntry);
                Log.v(LOG_TAG, "RESTAURANT URI IS " + restaurantInsertUri);

                Toast.makeText(getActivity(), "Successfully saved!", Toast.LENGTH_LONG).show();

                // Displays a message to the user encouraging them to store data with actual restaurant
                // names for a better experience
                if (name.equalsIgnoreCase("Empty")) {
                    displayAlert();
                } else{
                    // Upon saving an entry, automatically finishes the activity and takes the user back
                    // to the home screen
                    getActivity().finish();
                }


            }
        });
    }

    /**
     * Displays an alert to the user encouraging them to store entries with an actual restaurant name
     */
    private void displayAlert() {
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.empty_warning_message)
                .setPositiveButton(R.string.warning_entry, null)
                .setTitle(R.string.restaurant_not_found_button)
                .create();
        builder.show();
    }
}