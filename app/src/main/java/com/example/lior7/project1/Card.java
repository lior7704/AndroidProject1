package com.example.lior7.project1;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public class Card extends AppCompatImageView{
    private int imageId;
    final static int DEFAULT_IMAGE_ID = R.drawable.icon;
    private boolean state = false; // MemoryImageView is with default image

    public Card(Context context){
        super(context);
    }

    public void setImageId (int img_id){
        this.imageId = img_id;
    }

    public int getImageId(){
        return imageId;
    }

    public boolean getState(){
        return state;
    }
    public void setState(boolean flag){
        this.state = flag;
    }
}
