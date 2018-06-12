package com.example.lior7.project1.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lior7.project1.Object_Classes.Card;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] images;

    public ImageAdapter(Context c, Integer[] images) {
        mContext = c;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    // create a new ImageSwitcher for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Card imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new Card(mContext);
            imageView.setPadding(5,5,5,5);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (Card) convertView;
        }
        imageView.setImageResource(Card.getDefaultImageId());
        imageView.setImageId(images[position]);
        return imageView;
    }

}
