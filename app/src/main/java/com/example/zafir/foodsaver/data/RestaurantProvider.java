package com.example.zafir.foodsaver.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Content Provider class for the database. The purpose of the content provider is to create
 * a simple interface to the backend data for any other part of the application, or potentially
 * an external application. It abstracts away the underlying backend data model (which, in this case,
 * is a SQLite table, but could have been NoSQL or a file, etc.).
 */
public class RestaurantProvider extends ContentProvider {
    private static final int RESTAURANT = 100;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private RestaurantDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RestaurantContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RestaurantContract.PATH_RESTAURANT, RESTAURANT);
        return matcher;
    }

    /**
     * Creates the database
     * @return
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new RestaurantDbHelper(getContext());
        return true;
    }

    /**
     *
     * @param uri of the path of the table
     * @param projection is the list of columns
     * @param selection is a set of criteria to filter the data on (rows)
     * @param selectionArgs arguments for the selection
     * @param sortOrder defining any sort order
     * @return a Cursor, which is the result of a query
     * Implements querying the database
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        // Implements the query in the database
        switch(sUriMatcher.match(uri)) {
            case RESTAURANT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RestaurantContract.RestaurantEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RESTAURANT:
                return RestaurantContract.RestaurantEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     *
     * @param uri
     * @param contentValues
     * @return Uri of the inserted data
     * Inserts the data into the database, again in a standard interface
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case RESTAURANT:
                long _id = db.insert(RestaurantContract.RestaurantEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = RestaurantContract.RestaurantEntry.buildRestaurantUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }

        return returnUri;
    }

    /**
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return an int representing the number of rows affected
     * Implements deleting from the database, in a standard interface
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case RESTAURANT:
                rowsDeleted = db.delete(RestaurantContract.RestaurantEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }

        if (null == selection || 0 != rowsDeleted) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case RESTAURANT:
                rowsUpdated = db.update(RestaurantContract.RestaurantEntry.TABLE_NAME,
                        contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }

        if (0 != rowsUpdated) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
