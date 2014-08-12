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

/**
 * Created by zafir on 8/9/14.
 */

public class FetchNearbyRestaurantsTask extends AsyncTask<String, Void, String[]> {
    private final String LOG_TAG = FetchNearbyRestaurantsTask.class.getSimpleName();
    private ArrayAdapter<String> mRestaurantAdapter;
    private final Context mContext;

    public FetchNearbyRestaurantsTask(Context context, ArrayAdapter<String> restaurantAdapter) {
        mContext = context;
        mRestaurantAdapter = restaurantAdapter;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getRestaurantDataFromJson(String restaurantJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String API_RESULTS = "results";
        final String API_NAME = "name";
        final String API_ADDRESS = "vicinity";
        final int API_LIMIT = 25;

        JSONObject restaurantJson = new JSONObject(restaurantJsonStr);
        JSONArray restaurantJsonArray = restaurantJson.getJSONArray(API_RESULTS);

        String[] resultStrs = new String[API_LIMIT];
        for (int i = 0; i < API_LIMIT; i++) {
            // For now, using the format "Day, description, hi/low"
            String name;
            String address;
            JSONObject restaurant;
            try {
                // Get the JSON object representing the day
                restaurant = restaurantJsonArray.getJSONObject(i);
            } catch (JSONException e) {
                break;
            }

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            name = restaurant.getString(API_NAME);
            address = restaurant.getString(API_ADDRESS);

            resultStrs[i] = name + " - " + address;
        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Restaurant entry: " + s);
        }
        return resultStrs;
    }

    @Override
    protected String[] doInBackground(String... params) {
        if (params.length == 0) return null;

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String restaurantJsonStr = null;
        String apiKey = "AIzaSyBKam4BAeK1LKbOPLgAEBtsIKdvwH5j0mw";
        String type = "restaurant";
        String radius = "1500";
        String distance = "distance";

        try {
            final String PLACES_API_BASE_URL =
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            final String LOCATION_PARAM = "location";
            final String RADIUS_PARAM = "radius";
            final String TYPES_PARAM = "types";
            final String KEY_PARAM = "key";
            final String SORT_PARAM = "rankby";


            Uri builtUri = Uri.parse(PLACES_API_BASE_URL).buildUpon()
                    .appendQueryParameter(LOCATION_PARAM, params[0] + "," + params[1])
                    .appendQueryParameter(SORT_PARAM, distance)
                    .appendQueryParameter(TYPES_PARAM, type)
                    .appendQueryParameter(KEY_PARAM, apiKey).build();
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI: !!!!!!!!!!! " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
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
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
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
            return getRestaurantDataFromJson(restaurantJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(String[] result) {
        if (result != null) {
            mRestaurantAdapter.clear();
            for (String restaurantStr: result) {
                mRestaurantAdapter.add(restaurantStr);
            }
        }
    }
}