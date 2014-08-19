package com.example.zafir.foodsaver;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class is used to retrieve places near a given GPS location via an asynchronous task
 * using the Google Places API
 */

public class FetchNearbyRestaurantsTask extends AsyncTask<String, Void, ArrayList<String>> {
    // Used for logging purposes
    private final String LOG_TAG = FetchNearbyRestaurantsTask.class.getSimpleName();
    // the ArrayAdapter used to display the data
    private ArrayAdapter<String> mRestaurantAdapter;
    // Activity context used for various methods
    private final Context mContext;

    /**
     *
     * @param context of the activity calling this task
     * @param restaurantAdapter adapter used to take the data from this task and display it in the UI
     */
    public FetchNearbyRestaurantsTask(Context context, ArrayAdapter<String> restaurantAdapter) {
        mContext = context;
        mRestaurantAdapter = restaurantAdapter;
    }

    /**
     * Take the string representing all the nearby restaurants in JSON Format and
     * pull out the data we need to construct the Strings needed for the list in the UI.
     * @param restaurantJsonStr string representing JSON result of API call
     * @return ArrayList of parsed JSON strings
     */
    private ArrayList<String> getRestaurantDataFromJson(String restaurantJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String API_RESULTS = "results";
        final String API_NAME = "name";
        final String API_ADDRESS = "vicinity";
        // This is the amount of results we cap. TO-DO remove magic number
        final int API_LIMIT = 25;

        // Convert the input string into a JSON object, and then find the results
        // array which we can parse for our strings
        JSONObject restaurantJson = new JSONObject(restaurantJsonStr);
        JSONArray restaurantJsonArray = restaurantJson.getJSONArray(API_RESULTS);

        ArrayList<String> resultStrs = new ArrayList<String>();
        for (int i = 0; i < API_LIMIT; i++) {
            // Using the format "Restaurant name - address"
            String name;
            String address;
            JSONObject restaurant;

            try {
                // Get the JSON object representing the restaurant
                restaurant = restaurantJsonArray.getJSONObject(i);
            } catch (JSONException e) {
                break;
            }

            // Get the name and address from the restaurant object
            name = restaurant.getString(API_NAME);
            address = restaurant.getString(API_ADDRESS);

            // If we find a null object, stop parsing the JSON because we've reached the end of the results
            if (name == null) break;

            resultStrs.add(name + " - " + address);
        }

        // Log the results
        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Restaurant entry: " + s);
        }
        return resultStrs;
    }

    /**
     *
     * @param params are the latitude and longitude strings. The method expects it to come in the format
     *               params[latitude, longitude]
     * @return ArrayList of parsed JSON strings
     * Makes the API call to Google Places to retrieve nearby restaurants in JSON format
     */
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        if (params.length == 0) return null;

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Strings representing parameters in the API call
        String restaurantJsonStr = null;
        String apiKey = "AIzaSyBKam4BAeK1LKbOPLgAEBtsIKdvwH5j0mw";
        String type = "restaurant";
        String radius = "1500";
        String distance = "distance";

        try {
            // Names of query parameters as defined by the Google Places API
            final String PLACES_API_BASE_URL =
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            final String LOCATION_PARAM = "location";
            final String RADIUS_PARAM = "radius";
            final String TYPES_PARAM = "types";
            final String KEY_PARAM = "key";
            final String SORT_PARAM = "rankby";

            // Construct the URL for the Places API query
            // Possible parameters are available at the Google Places API page, at
            // https://developers.google.com/places/documentation/search#PlaceSearchRequests
            Uri builtUri = Uri.parse(PLACES_API_BASE_URL).buildUpon()
                    .appendQueryParameter(LOCATION_PARAM, params[0] + "," + params[1])
                    .appendQueryParameter(SORT_PARAM, distance)
                    .appendQueryParameter(TYPES_PARAM, type)
                    .appendQueryParameter(KEY_PARAM, apiKey).build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI: !!!!!!!!!!! " + builtUri.toString());

            // Create the request to Google Places, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            restaurantJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Nearby restaurants JSON String: " + restaurantJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            // Parse the JSON string into an array of strings that our adapter needs to display in the UI
            return getRestaurantDataFromJson(restaurantJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After the API call is done and parsed, this method works back in the UI thread
     * @param result is the ArrayList of strings that our adapter requires
     */
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        // Updates the adapter
        if (result != null) {
            mRestaurantAdapter.clear();
            for (String restaurantStr: result) {
                mRestaurantAdapter.add(restaurantStr);
            }
        }
        //Activity activity = (Activity) mContext;
        //activity.setProgressBarIndeterminateVisibility(false);
    }
}