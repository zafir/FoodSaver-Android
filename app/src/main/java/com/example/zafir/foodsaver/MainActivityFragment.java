package com.example.zafir.foodsaver;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment of MainActivity - displaying two tappable TextViews to either save a new entry
 * or view existing entries
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Makes the textview to save a new entry tappable. Launches a new activity on a phone,
        // or replaces the fragment in the two-pane tablet UI
        TextView saveButton = (TextView) rootView.findViewById(R.id.save_food_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().findViewById(R.id.restaurant_detail_container) != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.restaurant_detail_container, new SaveFoodFragment())
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity(), SaveFoodActivity.class);
                    startActivity(intent);
                }
            }
        });

        //Makes the textview to view existing entries tappable. Launches a new activity
        TextView myFoodbutton = (TextView) rootView.findViewById(R.id.my_food_button);
        myFoodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().findViewById(R.id.restaurant_detail_container) != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.restaurant_detail_container, new MyFoodFragment())
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity(), MyFoodActivity.class);
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }


}
