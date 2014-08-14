package com.example.zafir.foodsaver;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zafir.foodsaver.data.RestaurantContract;

public class MyItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MyItemFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MyItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private TextView mDateView;
        private TextView mNameView;
        private TextView mItemView;
        private RatingBar mRatingBar;
        private TextView mDescView;

        private static final int MYITEM_LOADER = 0;

        private static final int COL_ID = 0;
        private static final int COL_DATE = 1;
        private static final int COL_NAME = 2;
        private static final int COL_ITEM = 3;
        private static final int COL_RATING = 4;
        private static final int COL_DESC = 5;

        private String _id;

        public MyItemFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_item, container, false);
            mDateView = (TextView) rootView.findViewById(R.id.my_item_date_textview);
            mNameView = (TextView) rootView.findViewById(R.id.my_item_name_textview);
            mItemView = (TextView) rootView.findViewById(R.id.my_item_item_textview);
            mRatingBar = (RatingBar) rootView.findViewById(R.id.my_item_ratingbar);
            mDescView = (TextView) rootView.findViewById(R.id.my_item_desc_textview);
            setHasOptionsMenu(true);

            //Toast.makeText(getActivity(), _id + "new act", Toast.LENGTH_SHORT).show();
            return rootView;
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                AlertDialog dialog = confirmDelete();
                dialog.show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private AlertDialog confirmDelete() {

            AlertDialog deleteDialog = new AlertDialog.Builder(getActivity())
                    //set message, title, and icon
                    .setTitle(R.string.confirm_delete)
                    .setMessage(R.string.confirm_delete_message)
                    .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {

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
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();

            return deleteDialog;

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(MYITEM_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Intent intent = getActivity().getIntent();
            _id = intent.getStringExtra(Intent.EXTRA_TEXT);
            String[] projection = {RestaurantContract.RestaurantEntry._ID,
                    RestaurantContract.RestaurantEntry.COLUMN_DATE,
                    RestaurantContract.RestaurantEntry.COLUMN_RESTAURANT_KEY,
                    RestaurantContract.RestaurantEntry.COLUMN_ITEM,
                    RestaurantContract.RestaurantEntry.COLUMN_RATING,
                    RestaurantContract.RestaurantEntry.COLUMN_DESC};
            return new CursorLoader(
                    getActivity(),
                    RestaurantContract.RestaurantEntry.CONTENT_URI,
                    projection,
                    RestaurantContract.RestaurantEntry._ID + "=?",
                    new String[] {_id},
                    null
            );

        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
            if (data != null && data.moveToFirst()) {
                // String date = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_DATETEXT));
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
        public void onLoaderReset(Loader<Cursor> cursorLoader) {

        }
    }
}
