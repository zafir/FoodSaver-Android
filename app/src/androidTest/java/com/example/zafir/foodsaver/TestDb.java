package com.example.zafir.foodsaver;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.zafir.foodsaver.data.RestaurantContract;
import com.example.zafir.foodsaver.data.RestaurantDbHelper;

/**
 * Created by zafir on 8/8/14.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(RestaurantDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new RestaurantDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        String testRestaurantName = "Cheesecake Factory";
        String testAddress = "101 Geary St.";
        String testDescription = "Great cheesecake!";

        RestaurantDbHelper dbHelper = new RestaurantDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY, testRestaurantName);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_ADDRESS, testAddress);
        values.put(RestaurantContract.RestaurantEntry.COLUMN_DESC, testDescription);

        long restaurantRowId;
        restaurantRowId = db.insert(RestaurantContract.RestaurantEntry.TABLE_NAME, null, values);

        assertTrue(restaurantRowId != -1);
        Log.d(LOG_TAG, "New row id: " + restaurantRowId);

        String[] columns = {
                RestaurantContract.RestaurantEntry._ID,
                RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY,
                RestaurantContract.RestaurantEntry.COLUMN_ADDRESS,
                RestaurantContract.RestaurantEntry.COLUMN_DESC
        };

        Cursor cursor = db.query(
                RestaurantContract.RestaurantEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
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

        dbHelper.close();


    }

}
