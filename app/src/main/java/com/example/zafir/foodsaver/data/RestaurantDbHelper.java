package com.example.zafir.foodsaver.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zafir on 8/8/14.
 */
public class RestaurantDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "restaurant.db";
    public RestaurantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RESTAURANT_TABLE =
                "CREATE TABLE " + RestaurantContract.RestaurantEntry.TABLE_NAME + " (" +
                        RestaurantContract.RestaurantEntry._ID + " INTEGER PRIMARY KEY," +
                        RestaurantContract.RestaurantEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                        RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY + " TEXT NOT NULL, " + //should be "unique not null"?
                        RestaurantContract.RestaurantEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                        RestaurantContract.RestaurantEntry.COLUMN_ITEM + " TEXT NOT NULL, " +
                        RestaurantContract.RestaurantEntry.COLUMN_RATING + " INTEGER NOT NULL, " +
                        RestaurantContract.RestaurantEntry.COLUMN_DESC + " TEXT NOT NULL" + " );";
        sqLiteDatabase.execSQL(SQL_CREATE_RESTAURANT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RestaurantContract.RestaurantEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
