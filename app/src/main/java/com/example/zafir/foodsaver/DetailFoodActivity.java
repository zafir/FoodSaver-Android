package com.example.zafir.foodsaver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * This activity is the detail screen for when a user saves a new food entry. The user arrives at this
 * activity having chosen a restaurant from the previous activity, and in this activity they actually
 * create and save the entry into their database.
 */
public class DetailFoodActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFoodFragment())
                    .commit();
        }
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
}
