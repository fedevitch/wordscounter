package com.lyubomyr.wordscounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lyubomyr on 18.05.18.
 */

public class DisplayResultsView extends AppCompatActivity {

    public static final String KEY_RESULTS = "RESULTS";
    public static final String KEY_STRING_RESULTS = "STRING_RESULTS";

    private String LOG_TAG = "RESULTS_VIEW";

    private Store store;

    protected String results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.results_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.view_title_results);

        Log.d(LOG_TAG, "started activity");

        //legacy
        /*
        Intent intent = getIntent();

        if (intent.hasExtra(KEY_STRING_RESULTS)){

            this.results = intent.getStringExtra(KEY_STRING_RESULTS);

            Log.d(LOG_TAG, "displaying results");
            Log.d(LOG_TAG, results);

            TextView resultsTextView = (TextView) findViewById(R.id.resultsText);
            resultsTextView.setText(results);
        }
        */

        Log.d(LOG_TAG, "trying to get data from store");

        this.store = Store.getInstance();

        CountResult results = this.store.getCountResult();

        Log.d(LOG_TAG, String.valueOf(results.getWordsVocabulary().size()));

        //ToDo: remove when table view ready
        String resultString = getResultString(results);
        TextView resultsTextView = (TextView) findViewById(R.id.resultsText);
        resultsTextView.setText(resultString);


    }

    private String getResultString(CountResult countResult){
        String result = "";

        for(int i = 0; i < countResult.getWordsVocabulary().size(); i++){
            Word word = countResult.getWordsVocabulary().get(i);
            result += word.getWord() + "    " + word.getAppearsCount() + "\n";
        }

        for(int i = 0; i < countResult.getCharsVocabulary().size(); i++){
            WordChar c = countResult.getCharsVocabulary().get(i);
            result += c.getWordCharacter() + "  " + c.getAppearsCount() + "\n";
        }

        result += "Words: " + countResult.getWordsCount() + "\n";
        result += "Chars: " + countResult.getCharsCount() + "\n";

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d(LOG_TAG, "item selected");
        Log.d(LOG_TAG, String.valueOf(item.getItemId()));

        switch (item.getItemId()) {
            case android.R.id.home:
                /*
                if (NavUtils.shouldUpRecreateTask(this.getParent(), this.getIntent())) {
                    Log.d(LOG_TAG, "recreate");
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this.getParent())
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(this.getParent().getParentActivityIntent())
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    Log.d(LOG_TAG, "nav");
                    //This activity is part of this app's task, so simply
                    //navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, this.getParentActivityIntent());
                }
                */
                //Log.d(LOG_TAG, "home");

                //NavUtils.navigateUpFromSameTask(this);

                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
