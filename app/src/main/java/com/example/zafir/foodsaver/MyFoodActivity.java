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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.zafir.foodsaver.data.RestaurantContract.RestaurantEntry;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_food, menu);
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
    public static class MyFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private SimpleCursorAdapter mMyFoodAdapter;
        public static final int COL_RESTAURANT_KEY = 0;
        private static final int MYFOOD_LOADER = 0;
        String _id;

        public MyFoodFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(MYFOOD_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            String[] from = new String[] {RestaurantEntry.COLUMN_RESTAURANT_KEY};
            int[] to = new int[] {R.id.list_item_myfood_textview};
            mMyFoodAdapter = new SimpleCursorAdapter(
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
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {RestaurantEntry._ID,
                    RestaurantEntry.COLUMN_RESTAURANT_KEY};
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
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mMyFoodAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mMyFoodAdapter.swapCursor(null);
        }
    }
}
