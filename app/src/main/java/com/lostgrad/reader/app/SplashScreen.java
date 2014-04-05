package com.lostgrad.reader.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    // A reference to the local object
    private SplashScreen local;
    private Handler handler = new Handler();
    private Runnable runnable;


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

            /*
            *It is possible to immediately start the activity using this:
            * this.startActivity(new Intent(this, SelectOptionsActivity.class));
            * However, we will use a delay, to make use of the splash screen:
            * (http://stackoverflow.com/questions/7965494/how-to-put-some-delay-in-calling-an-activity-from-another-activity)
            */

            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SplashScreen.this, SelectOptionsActivity.class);
                    //launch select options page
                    startActivity(i);
                    finish();
                }
            }, 2000);

        }

    }

}
