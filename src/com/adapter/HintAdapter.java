package com.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

public class HintAdapter extends ArrayAdapter<String> {
    public HintAdapter(Context context, int resource, String[] videoNames) {
        super(context, resource, videoNames);
    }

    @Override
    public int getCount() {
        return super.getCount(); // This makes the trick: do not show last item
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}