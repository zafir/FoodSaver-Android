package com.example.zafir.foodsaver;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zafir.foodsaver.data.RestaurantContract;

/**
 * Fragment used by MyItemActivity - retrieves data from the database for a particular entry and
 * displays it for the user. Now you can remember what you ate!
 */
public class MyItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // The TextViews that will be populated with data
    private TextView mDateView;
    private TextView mNameView;
    private TextView mItemView;
    private RatingBar mRatingBar;
    private TextView mDescView;

    // Constant defined since there can be multiple loaders
    private static final int MYITEM_LOADER = 0;

    // Constants for the columns in the database.
    private static final int COL_ID = 0;
    private static final int COL_DATE = 1;
    private static final int COL_NAME = 2;
    private static final int COL_ITEM = 3;
    private static final int COL_RATING = 4;
    private static final int COL_DESC = 5;

    // Id of the item's row in the database, passed in via the intent
    private String _id;

    public MyItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_item, container, false);

        // Initialize the View objects
        mDateView = (TextView) rootView.findViewById(R.id.my_item_date_textview);
        mNameView = (TextView) rootView.findViewById(R.id.my_item_name_textview);
        mItemView = (TextView) rootView.findViewById(R.id.my_item_item_textview);
        mRatingBar = (RatingBar) rootView.findViewById(R.id.my_item_ratingbar);
        mDescView = (TextView) rootView.findViewById(R.id.my_item_desc_textview);

        // Indicates to Android system that the fragment has an options menu to display
        setHasOptionsMenu(true);

        return rootView;
    }



    @Override
    /**
     * Handles when a menu option is tapped
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            AlertDialog dialog = confirmDelete();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Deletes the entry if the user confirms that this is what they want to do
     * @return AlertDialog object which asks the user to confirm whether to delete the given item
     */
    private AlertDialog confirmDelete() {

        AlertDialog deleteDialog = new AlertDialog.Builder(getActivity())

                // Sets the AlertDialog's title, the message, and handles the positive and
                // negative choices
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {

                    // Deletes the entry from the database. The query finds the row with the given
                    // id. After deleting, it finishes the activity.
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getActivity().getContentResolver().delete(
                                RestaurantContract.RestaurantEntry.CONTENT_URI,
                                RestaurantContract.RestaurantEntry._ID + "=?",
                                new String[] {_id}
                        );
                        getActivity().finish();
                        dialog.dismiss();
                    }

                })

                        // Dismisses the dialog without taking further action if the user chooses not
                        // to delete
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return deleteDialog;

    }

    @Override
    /**
     * Initializes the loader to perform the database query and return its data
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MYITEM_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    /**
     * Required method for the loader. Performs the desired database query
     */
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Gets the intent that launched this activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {

            // The row Id should be passed in a string extra. This
            // allows us to know which row to pull the data from
            // for this entry
            _id = intent.getStringExtra(Intent.EXTRA_TEXT);

            // The columns we need in our query
            String[] projection = {RestaurantContract.RestaurantEntry._ID,
                    RestaurantContract.RestaurantEntry.COLUMN_DATE,
                    RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY,
                    RestaurantContract.RestaurantEntry.COLUMN_ITEM,
                    RestaurantContract.RestaurantEntry.COLUMN_RATING,
                    RestaurantContract.RestaurantEntry.COLUMN_DESC};

            //Performs the database query, with the given id and columns
            return new CursorLoader(
                    getActivity(),
                    RestaurantContract.RestaurantEntry.CONTENT_URI,
                    projection,
                    RestaurantContract.RestaurantEntry._ID + "=?",
                    new String[]{_id},
                    null
            );
        } else {
            return null;
        }
    }

    @Override
    /**
     * Required method for loader. Defines what to do when the Loader is finished
     * with the query. Populates the Views with the returned data
     */
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            // Gets the data for the respective fields, binds it to their respective views
            String date = data.getString(COL_DATE);
            mDateView.setText(date);

            String name = data.getString(COL_NAME);
            mNameView.setText(name);

            String item = data.getString(COL_ITEM);
            mItemView.setText(item);

            int rating = data.getInt(COL_RATING);
            mRatingBar.setRating(rating);

            String desc = data.getString(COL_DESC);
            mDescView.setText(desc);

        }
    }

    @Override
    /**
     * Required method for the loader. Not needed in our case.
     */
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
