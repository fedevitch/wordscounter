package com.lyubomyr.wordscounter;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.lyubomyr.wordscounter.Storage.SavedResultEntity;
import com.lyubomyr.wordscounter.Storage.SavedResultJoined;
import com.lyubomyr.wordscounter.Storage.SavedResultsViewModel;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyubomyr on 18.05.18.
 */

public class DisplayResultsView extends AppCompatActivity {

    public static final String KEY_RESULTS = "RESULTS";
    public static final String KEY_STRING_RESULTS = "STRING_RESULTS";

    private String LOG_TAG = "RESULTS_VIEW";

    private final Store store = Store.getInstance();

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

        CountResult results = this.store.getCountResult();

        Log.d(LOG_TAG, String.valueOf(results.getWordsVocabulary().size()));

        //ToDo: remove when table view ready
        String resultString = getResultString(results);
        TextView resultsTextView = (TextView) findViewById(R.id.resultsText);
        resultsTextView.setText(resultString);

        final WebView chartWebView = findViewById(R.id.chart_webview);


        chartWebView.getSettings().setJavaScriptEnabled(true);

        chartWebView.loadUrl("file:android_asset/chart.html");

        chartWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                Log.d(LOG_TAG, "Chart loading finished");
                Log.d(LOG_TAG, "Trying to execute js");

                chartWebView.evaluateJavascript(getChartWebViewData(), null);
            }
        });

        handleSaveResult(results);


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

    private void handleSaveResult(final CountResult result){

        Runnable r = new Runnable() {
            @Override
            public void run() {

                try {
                    if (store.getSettings().getSaveResults() == null) {
                        SettingsViewModel settingsViewModel = ViewModelProviders.of(DisplayResultsView.this).get(SettingsViewModel.class);

                        Settings settings = new Settings(settingsViewModel);

                        store.setSettings(settings);

                        Log.d(LOG_TAG, "Settings set");
                    }
                    if (store.getSettings().getSaveResults()) {
                        SavedResultsViewModel savedResultsViewModel = ViewModelProviders.of(DisplayResultsView.this).get(SavedResultsViewModel.class);
                        List<SavedResultJoined> savedResults = savedResultsViewModel.getCountResults();

                        if (savedResults.size() >= store.getSettings().getNumOfRecords()) {
                            savedResultsViewModel.deleteSavedResult(savedResults.get(savedResults.size() - 1).id);
                        }

                        savedResultsViewModel.saveCountResult(result.getDbEntity());
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Сталасі хюйня");
                }


            }
        };

        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch(Exception e){
            Log.e(LOG_TAG, "Тред не дочекався");
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    private String getChartWebViewData(){
        return
                "console.log('started'); console.log(JSON.stringify(google)); console.log(google.charts);" +
                //"var loadChart = function(){" +
                "      google.charts.load('current', {'packages':['treemap']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "      function drawChart() {\n" +
                        "console.log('drawChart called...');" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                "          ['Location', 'Parent', 'Market trade volume (size)', 'Market increase/decrease (color)'],\n" +
                "          ['Global',    null,                 0,                               0],\n" +
                "          ['America',   'Global',             0,                               0],\n" +
                "          ['Europe',    'Global',             0,                               0],\n" +
                "          ['Asia',      'Global',             0,                               0],\n" +
                "          ['Australia', 'Global',             0,                               0],\n" +
                "          ['Africa',    'Global',             0,                               0],\n" +
                "          ['Brazil',    'America',            11,                              10],\n" +
                "          ['USA',       'America',            52,                              31],\n" +
                "          ['Mexico',    'America',            24,                              12],\n" +
                "          ['Canada',    'America',            16,                              -23],\n" +
                "          ['France',    'Europe',             42,                              -11],\n" +
                "          ['Germany',   'Europe',             31,                              -2],\n" +
                "          ['Sweden',    'Europe',             22,                              -13],\n" +
                "          ['Italy',     'Europe',             17,                              4],\n" +
                "          ['UK',        'Europe',             21,                              -5],\n" +
                "          ['China',     'Asia',               36,                              4],\n" +
                "          ['Japan',     'Asia',               20,                              -12],\n" +
                "          ['India',     'Asia',               40,                              63],\n" +
                "          ['Laos',      'Asia',               4,                               34],\n" +
                "          ['Mongolia',  'Asia',               1,                               -5],\n" +
                "          ['Israel',    'Asia',               12,                              24],\n" +
                "          ['Iran',      'Asia',               18,                              13],\n" +
                "          ['Pakistan',  'Asia',               11,                              -52],\n" +
                "          ['Egypt',     'Africa',             21,                              0],\n" +
                "          ['S. Africa', 'Africa',             30,                              43],\n" +
                "          ['Sudan',     'Africa',             12,                              2],\n" +
                "          ['Congo',     'Africa',             10,                              12],\n" +
                "          ['Zaire',     'Africa',             8,                               10]\n" +
                "        ]);\n" +
                "\n" +
                "        tree = new google.visualization.TreeMap(document.getElementById('chart_div'));\n" +
                "\n" +   "console.log('drawing tree');" +
                "        tree.draw(data, {\n" +
                "          minColor: '#f00',\n" +
                "          midColor: '#ddd',\n" +
                "          maxColor: '#0d0',\n" +
                "          headerHeight: 15,\n" +
                "          fontColor: 'black',\n" +
                "          showScale: true\n" +
                "        });\n" +
                "\n" + "console.log('done');" +
                //"      }\n"+
                "      }\n";
    }



}
