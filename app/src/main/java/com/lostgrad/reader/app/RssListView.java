package com.lostgrad.reader.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lostgrad.reader.data.RssItem;
import com.lostgrad.reader.listeners.ListListener;
import com.lostgrad.reader.util.AlternateRowAdapter;
import com.lostgrad.reader.util.RssReader;

import java.util.List;

public class RssListView extends Activity {


    /**
     * Update: Downloading RSS data in an async task
     *
     * Create the main application view
     * @param savedInstanceState
     */

    // A reference to the local object
    private RssListView local;
    private static String rssUrl;
    private TextView txtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view
        setContentView(R.layout.activity_rss_list_view);

        buildRssUrl();

        local=this;
        GetRSSDataTask task = new GetRSSDataTask();
        // Start download RSS task
        task.execute(rssUrl);

        // Debug the thread name
        Log.d("RSS Reader", Thread.currentThread().getName());
    }

    /**
     * setRSS Search
     * builds the search string which is sent to jSoup
     */
    private void buildRssUrl() {
        Intent intent = getIntent();
            String profession="";
            String jobType="";
            String searchQuery="";

        if(intent.getExtras()!=null){
            if(intent.getExtras().getString("professionLink")!=null){
                profession = intent.getExtras().getString("professionLink");
            }
            if(intent.getExtras().getString("jobTypeLink")!=null){
                jobType= intent.getExtras().getString("jobTypeLink");
                jobType=jobType.replaceAll(" ", "-").toLowerCase();
            }
            if(intent.getExtras().getString("searchLink")!=null){
                searchQuery= intent.getExtras().getString("searchLink");
                searchQuery=searchQuery.replaceAll(" ", "+").toLowerCase();
            }


            this.rssUrl ="http://lostgrad.com/graduate-job/feed/?profession="+profession+"-jobs&job-type="+jobType+"&s="+searchQuery;
        }
    }

    /**
     * Class: GetRSSDataTask
     *
     */
    private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
        @Override
        protected List<RssItem> doInBackground(String... urls) {

            /*
            *this changes the text of the loading dialog
            */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtView.setText("Loading your selections");
                }
            });

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
            ArrayAdapter<RssItem> adapter = new AlternateRowAdapter(local,R.layout.list_layout, result);
            // Set list adapter for the ListView
            items.setAdapter(adapter);

            // Set list view item click listener
            items.setOnItemClickListener(new ListListener(result, local));
            progressDialog.dismiss();

        }


        /**
         * override preexecute to show loading animation
         */
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(RssListView.this, AlertDialog.THEME_HOLO_DARK);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_loading_dialog);

            //do initialization of required objects objects here
            txtView = (TextView) progressDialog.findViewById(R.id.textView2);
        };
    }

}


