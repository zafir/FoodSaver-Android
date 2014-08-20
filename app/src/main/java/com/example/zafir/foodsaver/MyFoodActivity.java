package com.example.zafir.foodsaver;

import android.app.Activity;
import android.os.Bundle;

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
}
