package com.lostgrad.reader.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class DisplayWebViewActivity extends ActionBarActivity {

    private WebView localWebView;

    //jsoup
    Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_web_view);

        Uri intentURL= getIntent().getData();

        if(intentURL==null)
            return;

        localWebView = (WebView) findViewById(R.id.webView);
        localWebView.getSettings().setJavaScriptEnabled(true);
        localWebView.setBackgroundColor(222);

        /**
         * Inserted this to prevent browser loading
         */
        localWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
               // Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
            }
        });

        //localWebView.loadUrl(intentURL.toString());

        JSoupAsync jSoupBrowser = new JSoupAsync(intentURL.toString());
        jSoupBrowser.execute();

    }



    public class JSoupAsync extends AsyncTask<Void, Void, String> {

        private String url;
        //overload the constructor to receive the url
        public JSoupAsync( String urlPassed)
        {
            this.url = urlPassed;
        }


        /**
         * override preexecute to show loading animation
         */
        ProgressDialog progressDialog;


        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(DisplayWebViewActivity.this, AlertDialog.THEME_HOLO_DARK);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Fetching job details");
            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_loading_dialog);

            //do initialization of required objects objects here
        };



        String table = null;

        @Override
        protected String doInBackground(Void... arg0) {

            try {
                doc = Jsoup.connect(this.url).timeout(5000).get();
                return table=this.getWebsiteData();

            } catch (IOException e) {
                table = "Error: Sorry, you're connection is a bit slow.";
                //e.printStackTrace();
            }
            return table;
        }

        protected void onPostExecute(String result) {


            String extraStyles=this.buildAdditionalStyles();

            localWebView.loadData(extraStyles+table, "text/html", "UTF-8");
            progressDialog.dismiss();

        }

        /**
         * getWebsiteData
         * @return String table
         */
        private String getWebsiteData(){

            //get table
            Element tbl = doc.select("div.single_datagrid").first();
            tbl.getElementById("single_ratings").remove();
            tbl.getElementById("review_button").remove();
            tbl.getElementsByClass("wpfp-span").remove();

            String table= tbl.toString();

            return table;

        }

        /**
         * buildAdditionalStyles
         * @return extra bit of styling for any html collected
         */
        private String buildAdditionalStyles(){

            String lgStyles= "<style>a{text-decoration:none; color:#222;}.single_datagrid{background:#fff;padding:10px;border:3px solid #ccc; margin:0px;} table{border-collapse:collapse;}table td{border: 1px solid #ccc; padding:3px; color:#222;} .pop-out-tbl{margin-bottom:15px;} .btn{color:#fff; background-color:#47a447; border-color: #398439; padding:8px;border-radius:7px;} #single_post_image{display:block;margin-left:auto;margin-right:auto;max-width:100%;margin-bottom:5px;}</style>";

            return lgStyles;

        }
    }
}
