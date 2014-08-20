package com.example.zafir.foodsaver;

import android.app.Activity;
import android.os.Bundle;

/**
 * The "homescreen" activity of the application. Connected to MainActivityFragment
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates a two-pane tablet UI showing saved entries by default
        // A button click can alternate what's displayed in the second pane
        if (findViewById(R.id.restaurant_detail_container) != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.restaurant_detail_container, new MyFoodFragment())
                    .commit();
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
        }
    }

}
