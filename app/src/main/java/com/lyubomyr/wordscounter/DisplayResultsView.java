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
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.Locale;
import java.util.Random;

/**
 * Created by lyubomyr on 18.05.18.
 */

public class DisplayResultsView extends AppCompatActivity {

    public static final String VIEWING_SAVED_RESULT = "ViewingSavedResult";

    private String LOG_TAG = "RESULTS_VIEW";

    private final Store store = Store.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        Toolbar toolbar = findViewById(R.id.results_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.view_title_results);

        CountResult results = this.store.getCountResult();

        buildResultsTable(results);

        handleSaveResult(results);

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

    private void buildResultsTable(CountResult countResult){
        int defaultPadding = 10;

        TableLayout tableLayout = findViewById(R.id.results_table);
        tableLayout.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

        TableRow charsTitleRow = new TableRow(DisplayResultsView.this);
        TextView charsTitleTextView = new TextView(DisplayResultsView.this);
        charsTitleTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

        charsTitleTextView.setText(String.format(Locale.getDefault(), "%s: %d", "Characters", countResult.getCharsCount()));
        charsTitleRow.addView(charsTitleTextView);

        tableLayout.addView(charsTitleRow);

        //loop for chars table
        for(WordChar wordChar: countResult.getCharsVocabulary()){
            TableRow charRow = new TableRow(DisplayResultsView.this);

            TextView charRowTextView = new TextView(DisplayResultsView.this);
            charRowTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
            charRowTextView.setText(String.format(Locale.getDefault(), "%c", wordChar.getWordCharacter()));
            charRow.addView(charRowTextView);

            TextView charCountTextView = new TextView(DisplayResultsView.this);
            charCountTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
            charCountTextView.setText(String.format(Locale.getDefault(), "%d", wordChar.getAppearsCount()));
            charRow.addView(charCountTextView);

            tableLayout.addView(charRow);
        }


        TableRow wordsTitleRow = new TableRow(DisplayResultsView.this);
        TextView wordsTitleTextView = new TextView(DisplayResultsView.this);
        wordsTitleTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
        wordsTitleTextView.setText(String.format(Locale.getDefault(), "%s: %d", "Words", countResult.getWordsCount()));
        wordsTitleRow.addView(wordsTitleTextView);

        tableLayout.addView(wordsTitleRow);

        //loop for words table
        for(Word word: countResult.getWordsVocabulary()){
            TableRow charRow = new TableRow(DisplayResultsView.this);

            TextView charRowTextView = new TextView(DisplayResultsView.this);
            charRowTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
            charRowTextView.setText(String.format(Locale.getDefault(), "%s", word.getWord()));
            charRow.addView(charRowTextView);

            TextView charCountTextView = new TextView(DisplayResultsView.this);
            charCountTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
            charCountTextView.setText(String.format(Locale.getDefault(), "%d", word.getAppearsCount()));
            charRow.addView(charCountTextView);

            tableLayout.addView(charRow);
        }


    }

    // deprecated
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
