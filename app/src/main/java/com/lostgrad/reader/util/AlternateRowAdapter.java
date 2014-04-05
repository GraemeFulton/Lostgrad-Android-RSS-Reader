package com.lostgrad.reader.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lostgrad.reader.data.RssItem;

import java.util.List;

public class AlternateRowAdapter extends ArrayAdapter<RssItem>{

    /* here we must override the constructor for ArrayAdapter
      * the only variable we care about now is ArrayList<RssItem> objects,
      * because it is the list of objects we want to display.
      */
    public AlternateRowAdapter(Context context, int layoutViewResourceId, List<RssItem> objects) {
        super(context, layoutViewResourceId, objects);
        //this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =super.getView(position, convertView, parent);

        TextView textView=(TextView) view.findViewById(android.R.id.text1);
        //parent.findViewById(R.id.)
            /*YOUR CHOICE OF COLOR*/
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTextSize(14);

        if(position%2==0)
        {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            view.setBackgroundColor(Color.parseColor("#BDF2E8"));

        }
        return view;
    }



}