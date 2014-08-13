package com.example.zafir.foodsaver.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by zafir on 8/8/14.
 */
public class RestaurantContract {

    public static final String CONTENT_AUTHORITY = "com.example.zafir.foodsaver.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RESTAURANT = "restaurant";

    public static final class RestaurantEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANT).build();

        public static final String TABLE_NAME = "restaurants";

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" +
                        PATH_RESTAURANT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" +
                        PATH_RESTAURANT;

        public static final String COLUMN_RESTAURANT_KEY = "restaurant_name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_ITEM = "item";
        public static final String COLUMN_DATE = "date";

        public static Uri buildRestaurantUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
