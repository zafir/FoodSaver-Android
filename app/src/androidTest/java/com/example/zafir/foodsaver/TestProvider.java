package com.example.zafir.foodsaver;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.zafir.foodsaver.data.RestaurantContract;
import com.example.zafir.foodsaver.data.RestaurantDbHelper;

/**
 * Created by zafir on 8/8/14.
 */
public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(RestaurantDbHelper.DATABASE_NAME);
    }

    public void testInsertReadProvider() {
        String testRestaurantName = "Cheesecake Factory";
        String testAddress = "101 Geary St.";
        String testDescription = "Great cheesecake!";
        String testDate = "20141208";
        String testItem = "Calimari";
        int testRating = 3;

        ContentValues values = new ContentValues();
        values.put(RestaurantContract.RestaurantEntry.COLUMN_DATE, testDate);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY, testRestaurantName);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS, testAddress);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_ITEM, testItem);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_RATING, testRating);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_DESC, testDescription);

        Uri insertUri = mContext.getContentResolver().insert(RestaurantContract.RestaurantEntry.CONTENT_URI, values);
        long restaurantRowId = ContentUris.parseId(insertUri);

        Log.d(LOG_TAG, "New row id: " + restaurantRowId);

        String[] columns = {
                RestaurantContract.RestaurantEntry._ID,
                RestaurantContract.RestaurantEntry.COLUMN_DATE,
                RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY,
                RestaurantContract.RestaurantEntry.COLUMN_ADDRESS,
                RestaurantContract.RestaurantEntry.COLUMN_ITEM,
                RestaurantContract.RestaurantEntry.COLUMN_RATING,
                RestaurantContract.RestaurantEntry.COLUMN_DESC
        };

        Cursor cursor = mContext.getContentResolver()
                .query(RestaurantContract.RestaurantEntry.CONTENT_URI,
                        null, //leaving columns null to return all colunns
                        null, //cols for "where" clause
                        null, //values for "where" clause
                        null // sort order
                );

        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_DATE);
            String date = cursor.getString(dateIndex);

            int nameIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY);
            String name = cursor.getString(nameIndex);

            int addressIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS);
            String address = cursor.getString(addressIndex);

            int itemIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_ITEM);
            String item = cursor.getString(itemIndex);

            int ratingIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_RATING);
            int rating = cursor.getInt(ratingIndex);

            int descriptionIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_DESC);
            String description = cursor.getString(descriptionIndex);

            assertEquals(testDate, date);
            assertEquals(testRestaurantName, name);
            assertEquals(testAddress, address);
            assertEquals(testItem, item);
            assertEquals(testRating, rating);
            assertEquals(testDescription, description);
        } else {
            fail("No values returned: ");
        }

    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(RestaurantContract.RestaurantEntry.CONTENT_URI);
        assertEquals(RestaurantContract.RestaurantEntry.CONTENT_TYPE, type);

    }
}
