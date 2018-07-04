package com.lyubomyr.wordscounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ChartCharsView extends AppCompatActivity {

    private String LOG_TAG = "Chars_chart_VIEW";

    private final Store store = Store.getInstance();

    private int[] colors = {
            Color.rgb(100,221,23),
            Color.rgb(128,0,128), Color.rgb(255,136,0),
            Color.rgb(255,0,0), Color.rgb(255,127,80),
            Color.rgb(47,95,255)
    };

    @Override
    protected void onCreate(Bundle savedInstance){

        super.onCreate(savedInstance);

        setContentView(R.layout.results_view_characters_chart);

        Toolbar toolbar = findViewById(R.id.results_chars_chart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildCharactersChart();

    }

    private void buildCharactersChart(){
        CountResult result = this.store.getCountResult();
        List<PieEntry> entries = new ArrayList<>();


        for(WordChar character : result.getCharsVocabulary()){
            PieEntry entry = new PieEntry((float) character.getAppearsCount(), character.getWordCharacter().toString());

            entries.add(entry);
        }

        PieDataSet characterSet = new PieDataSet(entries, "Characters");

        characterSet.setColors(colors);
        PieData characterData = new PieData(characterSet);
        PieChart characterChart = findViewById(R.id.characters_chart);
        characterChart.setUsePercentValues(false);
        characterChart.setCenterText("Characters");
        characterChart.setData(characterData);
        characterChart.invalidate();
    }

}
