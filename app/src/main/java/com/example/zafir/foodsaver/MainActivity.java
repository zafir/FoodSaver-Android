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
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
        }
    }

}
