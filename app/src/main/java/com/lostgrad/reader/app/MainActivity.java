package com.lostgrad.reader.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lostgrad.reader.data.RssItem;
import com.lostgrad.reader.listeners.ListListener;
import com.lostgrad.reader.util.RssReader;

import java.util.List;

public class MainActivity extends Activity {


    /**
     * Update: Downloading RSS data in an async task
     *
     * Create the main application view
     * @param savedInstanceState
     */

    // A reference to the local object
    private MainActivity local;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view
        setContentView(R.layout.activity_main);

        // Set reference to this activity
        local = this;

        GetRSSDataTask task = new GetRSSDataTask();

        // Start download RSS task
        task.execute("http://lostgrad.com/graduate-job/feed/?profession=computing-jobs");

        // Debug the thread name
        Log.d("RSS Reader", Thread.currentThread().getName());

    }

    private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
        @Override
        protected List<RssItem> doInBackground(String... urls) {

            // Debug the task thread name
            Log.d("RSS Reader", Thread.currentThread().getName());

            try {
                // Create RSS reader
                RssReader rssReader = new RssReader(urls[0]);

                // Parse RSS, get items
                return rssReader.getItems();

            } catch (Exception e) {
                Log.e("RSS Reader", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> result) {

            // Get a ListView from main view
            ListView items = (ListView) findViewById(R.id.listMainView);

            // Create a list adapter
            ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,android.R.layout.simple_list_item_1, result);
            // Set list adapter for the ListView
            items.setAdapter(adapter);

            // Set list view item click listener
            items.setOnItemClickListener(new ListListener(result, local));
        }
    }

}
