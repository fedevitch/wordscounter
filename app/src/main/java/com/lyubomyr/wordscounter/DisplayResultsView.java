package com.lyubomyr.wordscounter;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lyubomyr.wordscounter.Storage.SavedResultJoined;
import com.lyubomyr.wordscounter.Storage.SavedResultsViewModel;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Random;

/**
 * Created by lyubomyr on 18.05.18.
 */

public class DisplayResultsView extends AppCompatActivity {

    public static final String VIEWING_SAVED_RESULT = "ViewingSavedResult";

    private String LOG_TAG = "RESULTS_VIEW";

    private final Store store = Store.getInstance();

    private int[] colors = {
            Color.rgb(100,221,23),
            Color.rgb(128,0,128), Color.rgb(255,136,0),
            Color.rgb(255,0,0), Color.rgb(255,127,80),
            Color.rgb(47,95,255)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.results_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.view_title_results);


        Log.d(LOG_TAG, "trying to get data from store");
        Log.d(LOG_TAG, String.valueOf(new Date().getTime()));

        CountResult results = this.store.getCountResult();

        Log.d(LOG_TAG, String.valueOf(results.getWordsVocabulary().size()));

//        if(store.getSettings().getDisplayChart()) {
//            buildCharactersChart();
//            buildWordsChart();
//        }

        Log.d(LOG_TAG, "charts built");
        Log.d(LOG_TAG, String.valueOf(new Date().getTime()));

        if(store.getSettings().getDisplayTable()) {
            //ToDo: remove when table view ready
            String resultString = getResultString(results);
            TextView resultsTextView = findViewById(R.id.resultsText);
            resultsTextView.setText(resultString);
        }

        Log.d(LOG_TAG, "table built");
        Log.d(LOG_TAG, String.valueOf(new Date().getTime()));

        handleSaveResult(results);

        Log.d(LOG_TAG, "handlesave Done");
        Log.d(LOG_TAG, String.valueOf(new Date().getTime()));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "item selected");
        Log.d(LOG_TAG, String.valueOf(item.getItemId()));

        switch(item.getItemId()){
            case R.id.results_view_menu_goto_words_chart:
                Log.d(LOG_TAG, "goto word chart activity");
                openWordsChart();
                return true;
            case R.id.results_view_menu_goto_chars_chart:
                Log.d(LOG_TAG, "goto chars activity");
                openCharsChart();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCharsChart(){
        Intent intent = new Intent(this, ChartCharsView.class);
        startActivity(intent);
    }

    private void openWordsChart(){
        Intent intent = new Intent(this, ChartWordsView.class);
        startActivity(intent);
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

    private void handleSaveResult(final CountResult result){



        Intent intent = getIntent();

        if (intent.hasExtra(VIEWING_SAVED_RESULT)) {

            Boolean noSave = false;

            try {
                noSave = intent.getExtras().getBoolean(VIEWING_SAVED_RESULT);
            } catch(NullPointerException e){
                e.printStackTrace();
            }
            if (noSave) return;

            Log.d(LOG_TAG, "checked intent extras");
        } else {
            return;
        }



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

}
