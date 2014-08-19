package com.example.zafir.foodsaver;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * A custom ArrayAdapter that inherits all of the functionality from its superclass. The one customization
 * is that it alternates the color of every other view to improve readability for the user.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;

    public CustomArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     * As the View is drawn, every other row is a light gray color.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position % 2 == 1) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        } else {
            view.setBackgroundColor(Color.WHITE);
        }
        return view;
    }
}
