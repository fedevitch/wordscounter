package com.lyubomyr.wordscounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChartWordsView extends AppCompatActivity {

    private String LOG_TAG = "Words_chart_VIEW";

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

        setContentView(R.layout.results_view_words_chart);

        Toolbar toolbar = findViewById(R.id.results_words_chart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildWordsChart();

    }

    private class WordChartYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        WordChartYAxisValueFormatter() {
            mFormat = new DecimalFormat("#########");
        }

        @Override
        public String getFormattedValue(float value, AxisBase yAxis){
            return mFormat.format(value);
        }

    }

    private class WordChartXAxisValueFormatter implements IAxisValueFormatter {

        private String[] values;

        WordChartXAxisValueFormatter(final String[] values){
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase xAxis){
            //Log.d(LOG_TAG, String.valueOf(value));
            //Log.d(LOG_TAG, String.valueOf((int) value));
            return this.values[(int) value];
        }

    }

    private void buildWordsChart(){
        CountResult result = this.store.getCountResult();
        List<BarEntry> entries = new ArrayList<>();

        BarData wordData = new BarData();

        int index = 0;
        List<String> lbls = new ArrayList<>();
        for(Word word: result.getWordsVocabulary()){
            BarEntry entry = new BarEntry(index++, (float) word.getAppearsCount());
            entries.add(entry);

            BarDataSet wordSet = new BarDataSet(entries, word.getWord());

            wordSet.setColor(colors[new Random().nextInt(colors.length)]);

            lbls.add(word.getWord());

            wordData.addDataSet(wordSet);
        }

        HorizontalBarChart wordsChart = findViewById(R.id.words_chart);


        YAxis leftAxis = wordsChart.getAxisLeft();
        leftAxis.setValueFormatter(new WordChartYAxisValueFormatter());
        leftAxis.setGranularity(1f);
        YAxis rightAxis = wordsChart.getAxisRight();
        rightAxis.setValueFormatter(new WordChartYAxisValueFormatter());
        rightAxis.setGranularity(1f);

        final String[] labels = lbls.toArray(new String[] {});
        XAxis xAxis = wordsChart.getXAxis();
        xAxis.setValueFormatter(new WordChartXAxisValueFormatter(labels));
        xAxis.setGranularity(0.3f);

        //wordsChart.getLayoutParams().height = result.getWordsVocabulary().size() * 100;
        wordsChart.setDrawValueAboveBar(true);


        wordsChart.setData(wordData);
        wordsChart.invalidate();
    }

}
