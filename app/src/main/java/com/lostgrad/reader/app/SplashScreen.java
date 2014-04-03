package com.lostgrad.reader.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lostgrad.reader.data.RssItem;
import com.lostgrad.reader.listeners.ListListener;
import com.lostgrad.reader.util.RssReader;

import java.util.List;

public class SplashScreen extends Activity {

    // A reference to the local object
    private SplashScreen local;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() == null) {
            // No connectivity - Show alert
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "No internet connection.")
                    .setTitle("Error :(")
                    .setCancelable(false)
                    .setPositiveButton("Exit",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    finish();
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            // Connected - Start parsing
            // Set reference to this activity
            local = this;

            GetRSSDataTask task = new GetRSSDataTask();

            // Start download RSS task
            task.execute("http://lostgrad.com/graduate-job/feed/?profession=computing-jobs");

            // Debug the thread name
            Log.d("RSS Reader", Thread.currentThread().getName());

        }

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

            setContentView(R.layout.activity_rss_list_view);

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
