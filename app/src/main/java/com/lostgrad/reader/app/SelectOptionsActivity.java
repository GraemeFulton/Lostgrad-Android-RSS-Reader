package com.lostgrad.reader.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SelectOptionsActivity extends ActionBarActivity {

    private SelectOptionsActivity local;
    private Spinner professionSpinner, jobTypeSpinner;
    private Button btnSubmit;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_options);

        addListenerOnButton();
    }

    /**
     * Button Listener
     * Listens for selected options and for when search button is pressed
     */
    private void addListenerOnButton() {

        //spinners
        professionSpinner = (Spinner) findViewById(R.id.spinner1);
        jobTypeSpinner= (Spinner) findViewById(R.id.spinner2);
        //search
        searchBox= (EditText)findViewById(R.id.editText);
        //buttons
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        /**
         * Button Onclick Listener
         */
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //set values from the spinners
                String professionLink = String.valueOf(professionSpinner.getSelectedItem());
                String jobTypeLink = String.valueOf(jobTypeSpinner.getSelectedItem());
                //value from search
                String searchLink= String.valueOf(searchBox.getText());
                //launch web view
                Intent launchRssListView = new Intent(SelectOptionsActivity.this, RssListView.class);
                launchRssListView.putExtra("professionLink", professionLink);
                launchRssListView.putExtra("jobTypeLink", jobTypeLink);
                launchRssListView.putExtra("searchLink", searchLink);


                startActivity(launchRssListView);
            }

        });
    }
}
