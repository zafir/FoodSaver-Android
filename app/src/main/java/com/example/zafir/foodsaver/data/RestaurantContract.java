package com.example.zafir.foodsaver.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for the SQLite database and content provider which defines database attributes
 * (table names, schema, column names) and URIs
 */
public class RestaurantContract {

    public static final String CONTENT_AUTHORITY = "com.example.zafir.foodsaver.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RESTAURANT = "restaurant";

    // Implements BaseColumns because this class automatically assigns each row
    // a unique ID, which is convenient for querying later
    public static final class RestaurantEntry implements BaseColumns {
        // URI pointing to the path of the Restaurant table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANT).build();

        // Table name
        public static final String TABLE_NAME = "restaurants";

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" +
                        PATH_RESTAURANT;

        // Names of the columns in the database
        public static final String COLUMN_RESTAURANT_KEY = "restaurant_name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_ITEM = "item";
        public static final String COLUMN_DATE = "date";

        // Builds URI pointing to a row with the given id
        public static Uri buildRestaurantUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
