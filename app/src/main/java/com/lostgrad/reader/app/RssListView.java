package com.lostgrad.reader.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lostgrad.reader.data.RssItem;
import com.lostgrad.reader.listeners.ListListener;
import com.lostgrad.reader.util.AlternateRowAdapter;
import com.lostgrad.reader.util.RssReader;

import java.util.List;

import static android.graphics.Color.parseColor;

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
    private static Integer paged=1;
    private static String profession="";
    private String jobType="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view
        setContentView(R.layout.activity_rss_list_view);

        buildRssUrl();

        local=this;
        GetRSSDataTask task = new GetRSSDataTask(false);
        // Start download RSS task
        task.execute(rssUrl);

        // Debug the thread name
        Log.d("RSS Reader", Thread.currentThread().getName());
    }

    @Override
    public void onBackPressed() {
        if (this.paged>1){
            RssListView.this.paged-=1;
            String page = String.valueOf(RssListView.this.paged);
            // Starting a new async task
            new GetRSSDataTask(true).execute(RssListView.this.rssUrl+"&paged="+page);
        }else{
            super.onBackPressed();
        }
    }

   /**
     * setRSS Search
     * builds the search string which is sent to jSoup
     */
    private void buildRssUrl() {
        Intent intent = getIntent();
            String prof="";
            String jobtype="";
            String searchQuery="";

        if(intent.getExtras()!=null){
            if(intent.getExtras().getString("professionLink")!=null){
                profession = intent.getExtras().getString("professionLink");
                prof="profession="+profession+"-jobs&";

                if(intent.getExtras().getString("professionLink").equals("All"))prof="";


            }
            if(intent.getExtras().getString("jobTypeLink")!=null){
                jobType= intent.getExtras().getString("jobTypeLink");
                jobType=jobType.replaceAll(" ", "-").toLowerCase();
                jobtype="job-type="+jobType+"&";

                if(intent.getExtras().getString("jobTypeLink").equals("All")){
                    jobtype="";
                }


            }
            if(intent.getExtras().getString("searchLink")!=null){
                searchQuery= intent.getExtras().getString("searchLink");
                searchQuery="s="+searchQuery.replaceAll(" ", "+").toLowerCase();

                if(intent.getExtras().getString("searchLink").equals(""))searchQuery="";

            }

            this.paged=1;//set page to first


            this.rssUrl ="http://lostgrad.com/graduate-job/feed/?"+prof+jobtype+searchQuery;
            Log.d("graylien",this.rssUrl);
        }
    }

    /**
     * Class: GetRSSDataTask
     *
     */
    private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {

        private boolean loadMoreTask=false;

        public GetRSSDataTask(boolean loadMoreTask){

            if (loadMoreTask==true)
                this.loadMoreTask=true;

        }
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

            // Create a list adapter
            ArrayAdapter<RssItem> adapter = new AlternateRowAdapter(local,R.layout.list_item, result, loadMoreTask);

            // Get a ListView from main view
            ListView listView = (ListView) findViewById(R.id.rssLV);

            // Set list adapter for the ListView
            listView.setAdapter(adapter);

            // Set list view item click listener
            listView.setOnItemClickListener(new ListListener(result, local));

            //////////////////////////////
            /**
             * Add button to List View
             */

            // LoadMore button
            Button btnLoadMore = new Button(local);
            btnLoadMore.setText("Load More");
            btnLoadMore.setBackgroundColor(parseColor("#ff1ecca9"));
            listView.setPaddingRelative(0,0,0,10);

            // Adding Load More button to lisview at bottom
            listView.addFooterView(btnLoadMore);
            //set listener
            loadMoreListener(btnLoadMore);

            View header = getLayoutInflater().inflate(R.layout.list_header_pagination, null);
            listView.addHeaderView(header);
            TextView headerText = (TextView)header.findViewById(R.id.paginationTextView);
            headerText.setText("Page "+RssListView.this.paged+" of "+RssListView.this.profession+" Jobs");
            //////////////////////////////////
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
        }

        /**
         * load more listener
         * @param btn
         */

        private void loadMoreListener(Button btn){

            /**
             * Listening to Load More button click event
             * */
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    RssListView.this.paged+=1;
                    String page = String.valueOf(RssListView.this.paged);
                    // Starting a new async task

                    new GetRSSDataTask(true).execute(RssListView.this.rssUrl+"&paged="+page);
                }
            });


        }

    }

}


