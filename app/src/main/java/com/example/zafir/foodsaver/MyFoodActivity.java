package com.example.zafir.foodsaver;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zafir.foodsaver.data.RestaurantContract.RestaurantEntry;

/**
 * Activity displaying the user's locally stored saved entries
 */
public class MyFoodActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MyFoodFragment())
                    .commit();
        }
    }

    /**
     * Fragment connected to MyFoodActivity. Retrieves locally stored saved entries and displays
     * them as a list. Tapping on an item launches a new activity with details for that item.
     */
    public static class MyFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        // CursorAdapter used to populate the ListView
        private CustomCursorAdapter mMyFoodAdapter;
        // Constant associated with a loader, since there can be multiple loaders
        private static final int MYFOOD_LOADER = 0;
        // Name of a column in the database
        String _id;

        public MyFoodFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            // Initializes the loader used to load data from the database
            getLoaderManager().initLoader(MYFOOD_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            // Cursor adapter maps data in the 'from' array into a view in the 'to' array
            String[] from = new String[] {RestaurantEntry.COLUMN_RESTAURANT_KEY};
            int[] to = new int[] {R.id.list_item_myfood_textview};

            // Initializes the adapter. Ties the appropriate ListView to the adapter.
            mMyFoodAdapter = new CustomCursorAdapter(
                    getActivity(),
                    R.layout.list_item_myfood,
                    null,
                    from,
                    to,
                    0
            );
            View rootView = inflater.inflate(R.layout.fragment_my_food, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.listview_myFood);
            listView.setAdapter(mMyFoodAdapter);

            // Launches the detail activity when an item in the list is tapped.
            // Passes the item's accompanying database row id as an intent extra
            // so the detail activity can then easily retrieve and display data from that row
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor cursor = (Cursor) mMyFoodAdapter.getItem(i);
                    _id = cursor.getString(cursor.getColumnIndex(RestaurantEntry._ID));
                    Intent intent = new Intent(getActivity(), MyItemActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, _id);
                    startActivity(intent);
                }
            });

            return rootView;

        }

        @Override
        /**
         * Required method for loader
         */
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            // The columns that the database query will access
            String[] projection = {RestaurantEntry._ID,
                    RestaurantEntry.COLUMN_RESTAURANT_KEY};

            // The database query to find the data for the view.
            // URI is the path to the table in the database
            return new CursorLoader(
                    getActivity(),
                    RestaurantEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );
        }

        @Override
        /**
         * Required method for loader. After the query is finished, passes the returned data from the
         * query to the adapter which displays it in the view
         */
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mMyFoodAdapter.swapCursor(cursor);
        }

        @Override
        /**
         * Required method for the loader. Not needed in our case.
         */
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mMyFoodAdapter.swapCursor(null);
        }
    }
}
