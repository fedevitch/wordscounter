package com.lyubomyr.wordscounter;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsView extends AppCompatActivity {

    private final String LOG_TAG = "SETTINGS_VIEW";

    private SettingsViewModel settingsViewModel;
    private Store store;

    private List<SettingsEntity> settingsEntityList;

    private ListView settingsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // toolbar.setTitle(R.string.view_title_settings);
        // toolbar.setSubtitle(R.string.view_title_settings);
        settingsListView = findViewById(R.id.settings_list);

        Log.d(LOG_TAG, "settings started");

        store = Store.getInstance();

        initializeSettings();


    }

    private void initializeSettings(){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                settingsViewModel = ViewModelProviders.of(SettingsView.this).get(SettingsViewModel.class);

                Settings settings = new Settings(settingsViewModel);

                store.setSettings(settings);

                Log.d(LOG_TAG, "Settings set");

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
        displayList(store.getSettings().settingsDb);


    }

    private void displayList(List<SettingsEntity> settings){
        Log.d(LOG_TAG, "displaying list");
        SettingsViewCellAdapter cellAdapter = new SettingsViewCellAdapter(this, settings);
        settingsListView.setAdapter(cellAdapter);
        settingsListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(LOG_TAG, "Item clicked");
            Log.d(LOG_TAG, String.valueOf(position));
        });
    }

}
