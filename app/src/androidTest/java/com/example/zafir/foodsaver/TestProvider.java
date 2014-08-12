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

        ContentValues values = new ContentValues();
        values.put(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY, testRestaurantName);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS, testAddress);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_DESC, testDescription);

        Uri insertUri = mContext.getContentResolver().insert(RestaurantContract.RestaurantEntry.CONTENT_URI, values);
        long restaurantRowId = ContentUris.parseId(insertUri);

        Log.d(LOG_TAG, "New row id: " + restaurantRowId);

        String[] columns = {
                RestaurantContract.RestaurantEntry._ID,
                RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY,
                RestaurantContract.RestaurantEntry.COLUMN_ADDRESS,
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
            int nameIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY);
            String name = cursor.getString(nameIndex);

            int addressIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS);
            String address = cursor.getString(addressIndex);

            int descriptionIndex = cursor.getColumnIndex(RestaurantContract.RestaurantEntry.COLUMN_DESC);
            String description = cursor.getString(descriptionIndex);

            assertEquals(testRestaurantName, name);
            assertEquals(testAddress, address);
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
