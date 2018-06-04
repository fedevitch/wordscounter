package com.lyubomyr.wordscounter;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsView extends AppCompatActivity {

    private String LOG_TAG = "SETTINGS_VIEW";


    private Store store;

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

        store = Store.getInstance();

        initializeSettings();


    }

    private void initializeSettings(){

        SettingsViewModel settingsViewModel = ViewModelProviders.of(SettingsView.this).get(SettingsViewModel.class);

        Settings settings = new Settings(settingsViewModel);

        store.setSettings(settings);

        Log.d(LOG_TAG, "Settings set");

    }

}
