package com.lostgrad.reader.listeners;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import com.lostgrad.reader.data.RssItem;

import java.util.List;

/**
 * Created by graeme on 02/04/14.
 */
public class ListListener implements AdapterView.OnItemClickListener {

    //List item's reference
    List<RssItem> listItems;
    //calling activity reference
    Activity activity;

    public ListListener(List<RssItem> aListItems, Activity anActivity){

        listItems = aListItems;
        activity = anActivity;

    }

    /**
     * Start a browser with url from the rss item.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(listItems.get(pos).getLink()));

        activity.startActivity(i);
    }
}
