package com.example.zafir.foodsaver;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * A custom CursorAdapter that inherits all of the functionality from its superclass. The one customization
 * is that it alternates the color of every other view to improve readability for the user.
 */
public class CustomCursorAdapter extends SimpleCursorAdapter {

    private Context mContext;


    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
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

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        if(cursor.getPosition() % 2 == 0) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        }else{
            view.setBackgroundColor(Color.WHITE);
        }
    }
}
