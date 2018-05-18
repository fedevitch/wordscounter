package com.lyubomyr.wordscounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lyubomyr on 18.05.18.
 */

public class DisplayResultsView extends AppCompatActivity {

    private String LOG_TAG = "RESULTS_VIEW";

    protected String results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        this.results = intent.getStringExtra("results");

        Log.d(LOG_TAG, "started activity");


        if (null != results){
            Log.d(LOG_TAG, "displaying results");
            Log.d(LOG_TAG, results);
            Toast.makeText(getApplicationContext(), "got results!!!", Toast.LENGTH_SHORT);
            Toast.makeText(getApplicationContext(), results, Toast.LENGTH_LONG);

            TextView resultsTextView = (TextView) findViewById(R.id.resultsText);
            resultsTextView.setText(results);
        }
    }

}
