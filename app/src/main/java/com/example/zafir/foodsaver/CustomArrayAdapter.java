package com.example.zafir.foodsaver;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by zafir on 8/13/14.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;

    public CustomArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

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
