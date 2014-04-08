package com.lostgrad.reader.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostgrad.reader.app.R;
import com.lostgrad.reader.data.RssItem;
import com.lostgrad.reader.imageLoader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AlternateRowAdapter extends ArrayAdapter<RssItem>{

    private Context context;
    private ImageView image;
    private TextView description;
    private TextView meta;
    private boolean loadMore = false;
    private List<RssItem> items = new ArrayList<RssItem>();

    //ImageLoader class borrowed from LazyList: https://github.com/thest1/LazyList
    public ImageLoader imageLoader;

    /**
     * AlternateRowAdapter
     * @param context
     * @param layoutViewResourceId
     * @param objects
     * @param loadMore - if true, we are appending data to an existing list
     */
    public AlternateRowAdapter(Context context, int layoutViewResourceId, List<RssItem> objects, boolean loadMore) {
        super(context, layoutViewResourceId, objects);
        this.context = context;
        this.items = objects;
        this.loadMore = loadMore;
        this.imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public RssItem getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row==null){
            //Row Inflation
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row= inflater.inflate(R.layout.list_item, parent, false);
        }
        //get item
        RssItem item = getItem(position);

        /**
         * Get References to Layout Ids
         */
        //get reference to ImageView
        image = (ImageView) row.findViewById(R.id.image);

        //get reference to TextView - Description
        description = (TextView) row.findViewById(R.id.description);

        //get reference to TextView - meta
   //     meta = (TextView) row.findViewById(R.id.meta);

        /**
         * Set data to references
         */
         description.setText(item.getTitle().toString());

        imageLoader.DisplayImage(item.getImageUrl(), image);

//        TextView textView=(TextView) view.findViewById(android.R.id.text1);
//        //parent.findViewById(R.id.)
//            /*YOUR CHOICE OF COLOR*/
//        textView.setTextColor(Color.parseColor("#333333"));
//        textView.setTextSize(14);
//
//        if(position%2==0)
//        {
//            view.setBackgroundColor(Color.parseColor("#ffffff"));
//        }else{
//            view.setBackgroundColor(Color.parseColor("#BDF2E8"));
//
//        }
//        return view;

        return row;
    }



}