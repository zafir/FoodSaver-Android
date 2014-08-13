package com.example.zafir.foodsaver;

/**
 * Created by zafir on 8/6/14.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView saveButton = (TextView) rootView.findViewById(R.id.save_food_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SaveFoodActivity.class);
                startActivity(intent);
            }
        });
        TextView myFoodbutton = (TextView) rootView.findViewById(R.id.my_food_button);
        myFoodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyFoodActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }


}
