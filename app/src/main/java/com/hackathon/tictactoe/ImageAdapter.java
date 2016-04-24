package com.hackathon.tictactoe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by macbook on 24.04.2016.
 */

public class ImageAdapter extends BaseAdapter
{
    int count = 9;
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    Context myContext;

    public ImageAdapter(Context context)
    {
        myContext = context;
        for(int i=0;i<count;i++)
        {
            ImageView tmp = new ImageView(myContext);
            tmp.setImageResource(R.drawable.blank_square);
            imageViews.add(tmp);
        }
    }

    @Override
    public int getCount()
    {
        return imageViews.size();
    }

    @Override
    public Object getItem(int position)
    {
        return imageViews.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return imageViews.get(position);
    }
}
