package com.lyubomyr.wordscounter;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.ArrayList;
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

                if(settingsEntityList.size() == 0){
                    settingsEntityList = getDefaultSettings();
                    settingsViewModel.setDefaultSettings(settingsEntityList);
                }

            }
        };


        Thread t = new Thread(init);

        t.start();

    }

    private List<SettingsEntity> getDefaultSettings(){
        List<SettingsEntity> settings = new ArrayList<SettingsEntity>();

        SettingsEntity saveResults = new SettingsEntity();
        saveResults.setting_name = "save_results";
        saveResults.setting_type = "switch";
        saveResults.setting_value = "true";
        settings.add(saveResults);

        SettingsEntity numOfRecords = new SettingsEntity();
        numOfRecords.setting_name = "num_of_records";
        numOfRecords.setting_type = "num_picker";
        numOfRecords.setting_value = "5";
        settings.add(numOfRecords);

        SettingsEntity ignoredSymbols = new SettingsEntity();
        ignoredSymbols.setting_name = "ignored_symbols";
        ignoredSymbols.setting_type = "text_modal";
        ignoredSymbols.setting_value = " .,;:!?+=*-~`  {(//<[]>)}\"\'|&\n\t  \\    ";
        settings.add(ignoredSymbols);

        SettingsEntity displayChart = new SettingsEntity();
        displayChart.setting_name = "display_chart";
        displayChart.setting_type = "checkbox";
        displayChart.setting_value = "true";
        settings.add(displayChart);

        SettingsEntity displayTable = new SettingsEntity();
        displayTable.setting_name = "display_table";
        displayTable.setting_type = "checkbox";
        displayTable.setting_value = "true";
        settings.add(displayTable);

        return settings;
    }

}
