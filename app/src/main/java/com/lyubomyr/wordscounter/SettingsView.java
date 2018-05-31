package com.lyubomyr.wordscounter;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.List;

public class SettingsView extends AppCompatActivity {

    private String LOG_TAG = "SETTINGS_VIEW";

    private SettingsViewModel settingsViewModel;

    private List<SettingsEntity> settingsEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitle(R.string.view_title_settings);
        toolbar.setSubtitle(R.string.view_title_settings);

        Log.d(LOG_TAG, "settings started");

        initializeSettings();


    }

    private void initializeSettings(){

        Runnable init = new Runnable() {
            @Override
            public void run() {

                settingsViewModel = ViewModelProviders.of(SettingsView.this).get(SettingsViewModel.class);

                settingsEntityList = settingsViewModel.getAllSettings();
                Log.d(LOG_TAG, "Got settings");
                Log.d(LOG_TAG, String.valueOf(settingsEntityList.size()));
            }
        };


        Thread t = new Thread(init);

        t.start();

    }

}
