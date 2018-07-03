package com.lyubomyr.wordscounter;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
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
import com.lyubomyr.wordscounter.Storage.SavedResultEntity;
import com.lyubomyr.wordscounter.Storage.SavedResultJoined;
import com.lyubomyr.wordscounter.Storage.SavedResultsViewModel;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lyubomyr on 18.05.18.
 */

public class DisplayResultsView extends AppCompatActivity {

    public static final String VIEWING_SAVED_RESULT = "ViewingSavedResult";

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


        Log.d(LOG_TAG, "trying to get data from store");

        CountResult results = this.store.getCountResult();

        Log.d(LOG_TAG, String.valueOf(results.getWordsVocabulary().size()));

        buildCharactersChart();
        buildWordsChart();

        //ToDo: remove when table view ready
        String resultString = getResultString(results);
        TextView resultsTextView = findViewById(R.id.resultsText);
        resultsTextView.setText(resultString);

        handleSaveResult(results);


    }

    private void buildCharactersChart(){
        CountResult result = this.store.getCountResult();
        List<PieEntry> entries = new ArrayList<>();


        for(WordChar character : result.getCharsVocabulary()){
            PieEntry entry = new PieEntry((float) character.getAppearsCount(), character.getWordCharacter().toString());

            entries.add(entry);
        }

        PieDataSet characterSet = new PieDataSet(entries, "Characters");
        int[] colors = { Color.rgb(100,221,23), Color.rgb(128,0,128), Color.rgb(255,136,0),
                Color.rgb(255,0,0), Color.rgb(255,127,80), Color.rgb(47,95,255)
        };
        characterSet.setColors(colors);
        PieData characterData = new PieData(characterSet);
        PieChart characterChart = findViewById(R.id.characters_chart);
        characterChart.setUsePercentValues(false);
        characterChart.setCenterText("Characters");
        characterChart.setData(characterData);
        characterChart.invalidate();
    }

    private void buildWordsChart(){
        CountResult result = this.store.getCountResult();
        List<BarEntry> entries = new ArrayList<>();

        BarData wordData = new BarData();

        int index = 0;
        for(Word word: result.getWordsVocabulary()){
            BarEntry entry = new BarEntry(index++, (float) word.getAppearsCount());
            entries.add(entry);

            BarDataSet wordSet = new BarDataSet(entries, word.getWord());

            wordData.addDataSet(wordSet);
        }

        HorizontalBarChart wordsChart = findViewById(R.id.words_chart);

        XAxis xAxis = wordsChart.getXAxis();
        xAxis.setGranularity(10f);

        wordsChart.getLayoutParams().height = result.getWordsVocabulary().size() * 50;
        wordsChart.setDrawValueAboveBar(true);


        wordsChart.setData(wordData);
        wordsChart.invalidate();
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
