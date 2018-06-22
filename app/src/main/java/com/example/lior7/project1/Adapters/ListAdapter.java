package com.example.lior7.project1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

import com.example.lior7.project1.Object_Classes.UserDetails;
import com.example.lior7.project1.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private Context context;

    public ListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        return mInflater.inflate(R.layout.user_details, null);
    }
}