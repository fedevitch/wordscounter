package com.lyubomyr.wordscounter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyubomyr.wordscounter.Storage.SavedResultJoined;
import com.lyubomyr.wordscounter.Storage.SavedResultsViewModel;
import com.lyubomyr.wordscounter.Storage.SettingsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class SavedCountsView extends AppCompatActivity {

    private final String LOG_TAG = "Saved counts view";

    private SavedResultsViewModel savedResultsViewModel;

    private ListView savedCountResultsListView;

    private List<CountResult> countResults;

    private SavedCountsViewCellAdapter cellAdapter;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.saved_results_view);

        Toolbar toolbar = findViewById(R.id.saved_results_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitle(R.string.view_title_settings);
        //toolbar.setSubtitle(R.string.view_title_settings);
        savedCountResultsListView = findViewById(R.id.saved_results_list);

        Log.d(LOG_TAG, "settings started");

        Store store = Store.getInstance();

        getSavedCountResults();
        //displayList(this.countResults);

    }

    private void getSavedCountResults(){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                savedResultsViewModel = ViewModelProviders.of(SavedCountsView.this).get(SavedResultsViewModel.class);


                List<SavedResultJoined> saveResultsDb = savedResultsViewModel.getCountResults();

                countResults = new ArrayList<>();
                for (SavedResultJoined savedResultJoined : saveResultsDb) {
                    countResults.add(new CountResult(savedResultJoined));
                }


                displayList(countResults);

                // LiveData
                savedResultsViewModel.getAllCountresults().observe(SavedCountsView.this, new Observer<List<SavedResultJoined>>() {
                    @Override
                    public void onChanged(@Nullable List<SavedResultJoined> savedResultJoineds) {
                        countResults = new ArrayList<>();
                        assert savedResultJoineds != null;
                        for (SavedResultJoined savedResultJoined : savedResultJoineds) {
                            countResults.add(new CountResult(savedResultJoined));
                        }

                        cellAdapter.setCountResultsList(countResults);

                    }
                });

            }
        };

        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch(Exception e){
            Log.e(LOG_TAG, "???????? ???? ??????????????????");
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    private void displayList(List<CountResult> results){
        Log.d(LOG_TAG, "displaying list");
        cellAdapter = new SavedCountsViewCellAdapter(this, results, savedResultsViewModel);
        savedCountResultsListView.setAdapter(cellAdapter);
    }

}
