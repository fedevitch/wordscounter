package com.lyubomyr.wordscounter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;
import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private Boolean saveResults;
    private int numOfRecords;

    private Boolean displayChart;
    private Boolean displayTable;

    private String ignoredSymbols;

    public List<SettingsEntity> settingsDb;
    private final SettingsViewModel settingsViewModel;
    private String LOG_TAG = "SETTINGS_HANDLER";

    public enum Names {
        save_results, num_of_records, ignored_symbols,
        display_chart, display_table
    }

    public enum Types {
        _switch, num_picker, text_modal, checkbox
    }

    public void saveSettings(){

        this.mapToDbEntity();
        Runnable save = new Runnable() {
            @Override
            public void run() {
                settingsViewModel.setDefaultSettings(settingsDb);
            }
        };

        Thread t = new Thread(save);

        t.start();

    }

    private void saveSetting(final SettingsEntity setting){
        Runnable save = new Runnable() {
            @Override
            public void run() {
                settingsViewModel.updateSetting(setting);
            }
        };

        Thread t = new Thread(save);

        t.start();
    }



    public Settings(final SettingsViewModel settingsViewModel){
        this.settingsViewModel = settingsViewModel;

        Runnable init = new Runnable() {
            @Override
            public void run() {

                settingsDb = settingsViewModel.getAllSettings();
                Log.d(LOG_TAG, "Got settings");
                Log.d(LOG_TAG, String.valueOf(settingsDb.size()));

                if(settingsDb.size() == 0){
                    Log.d(LOG_TAG, "Setting default settings");
                    settingsDb = getDefaultSettings();
                    settingsViewModel.setDefaultSettings(settingsDb);
                }

                mapFromDbEntity(settingsDb);

            }
        };

        Thread t = new Thread(init);

        t.start();

        try {
            t.join();
        } catch(Exception e){
            Log.e(LOG_TAG, "Тред не дочекався");
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void mapFromDbEntity(@NonNull List<SettingsEntity> settingsDb){

        for(SettingsEntity setting: settingsDb){
            try {
                if (setting.setting_name.equals(Names.display_chart.toString())) {
                    this.displayChart = setting.setting_value.equals("true");
                } else if (setting.setting_name.equals(Names.display_table.toString())) {
                    this.displayTable = setting.setting_value.equals("true");
                } else if (setting.setting_name.equals(Names.save_results.toString())) {
                    this.saveResults = setting.setting_value.equals("true");
                } else if (setting.setting_name.equals(Names.num_of_records.toString())) {
                    this.numOfRecords = Integer.valueOf(setting.setting_value);
                } else if (setting.setting_name.equals(Names.ignored_symbols.toString())) {
                    this.ignoredSymbols = setting.setting_value;
                }
            } catch (Exception e){
                Log.e(LOG_TAG, "mapFromDbEntity error");
                Log.e(LOG_TAG, setting.setting_name);
                Log.e(LOG_TAG, setting.setting_value);

                e.printStackTrace();
            }
        }

    }

    private void mapToDbEntity(){
        for(SettingsEntity setting: settingsDb){
            try {
                if (setting.setting_name.equals(Names.display_chart.toString())) {
                    setting.setting_value = this.displayChart.toString();
                } else if (setting.setting_name.equals(Names.display_table.toString())) {
                    setting.setting_value = this.displayTable.toString();
                } else if (setting.setting_name.equals(Names.save_results.toString())) {
                    setting.setting_value = this.saveResults.toString();
                } else if (setting.setting_name.equals(Names.num_of_records.toString())) {
                    setting.setting_value = String.valueOf(this.numOfRecords);
                } else if (setting.setting_name.equals(Names.ignored_symbols.toString())) {
                    setting.setting_value = this.ignoredSymbols;
                }
            } catch (Exception e){
                Log.e(LOG_TAG, "mapToDbEntity error");
                Log.e(LOG_TAG, setting.setting_name);
                e.printStackTrace();
            }
        }
    }

    private List<SettingsEntity> getDefaultSettings(){
        List<SettingsEntity> settings = new ArrayList<SettingsEntity>();

        SettingsEntity saveResults = new SettingsEntity();
        saveResults.setting_name = Names.save_results.toString();
        saveResults.setting_type = Types._switch.toString();
        saveResults.setting_value = "true";
        settings.add(saveResults);

        SettingsEntity numOfRecords = new SettingsEntity();
        numOfRecords.setting_name = Names.num_of_records.toString();
        numOfRecords.setting_type = Types.num_picker.toString();
        numOfRecords.setting_value = "5";
        settings.add(numOfRecords);

        SettingsEntity ignoredSymbols = new SettingsEntity();
        ignoredSymbols.setting_name = Names.ignored_symbols.toString();
        ignoredSymbols.setting_type = Types.text_modal.toString();
        ignoredSymbols.setting_value = " .,;:!?+=*-~`  {(//<[]>)}\"\'|&\n\t  \\    ";
        settings.add(ignoredSymbols);

        SettingsEntity displayChart = new SettingsEntity();
        displayChart.setting_name = Names.display_chart.toString();
        displayChart.setting_type = Types.checkbox.toString();
        displayChart.setting_value = "true";
        settings.add(displayChart);

        SettingsEntity displayTable = new SettingsEntity();
        displayTable.setting_name = Names.display_table.toString();
        displayTable.setting_type = Types.checkbox.toString();
        displayTable.setting_value = "true";
        settings.add(displayTable);

        return settings;
    }

    public Boolean getDisplayChart() {
        return displayChart;
    }

    public Boolean getDisplayTable() {
        return displayTable;
    }

    public Boolean getSaveResults() {
        return saveResults;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public String getIgnoredSymbols() {
        return ignoredSymbols;
    }

    public void setSaveResults(Boolean value){

        SettingsEntity setting = new SettingsEntity();
        setting.setting_name = Names.save_results.toString();
        setting.setting_type = Types._switch.toString();
        setting.setting_value = value.toString();

        try {
            saveSetting(setting);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error saving");
            Log.e(LOG_TAG, setting.setting_name);
            e.printStackTrace();
        } finally {
            this.saveResults = value;
        }

    }

    public void setDisplayChart(Boolean displayChart) {

        SettingsEntity setting = new SettingsEntity();
        setting.setting_name = Names.display_chart.toString();
        setting.setting_type = Types.checkbox.toString();
        setting.setting_value = displayChart.toString();

        try {
            saveSetting(setting);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error saving");
            Log.e(LOG_TAG, setting.setting_name);
            e.printStackTrace();
        } finally {
            this.displayChart = displayChart;
        }

    }

    public void setDisplayTable(Boolean displayTable) {

        SettingsEntity setting = new SettingsEntity();
        setting.setting_name = Names.display_table.toString();
        setting.setting_type = Types.checkbox.toString();
        setting.setting_value = displayTable.toString();

        try {
            saveSetting(setting);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error saving");
            Log.e(LOG_TAG, setting.setting_name);
            e.printStackTrace();
        } finally {
            this.displayTable = displayTable;
        }

    }

    public void setIgnoredSymbols(String ignoredSymbols) {

        SettingsEntity setting = new SettingsEntity();
        setting.setting_name = Names.save_results.toString();
        setting.setting_type = Types.text_modal.toString();
        setting.setting_value = ignoredSymbols;

        try {
            saveSetting(setting);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error saving");
            Log.e(LOG_TAG, setting.setting_name);
            e.printStackTrace();
        } finally {
            this.ignoredSymbols = ignoredSymbols;
        }

    }

    public void setNumOfRecords(int numOfRecords) {

        SettingsEntity setting = new SettingsEntity();
        setting.setting_name = Names.save_results.toString();
        setting.setting_type = Types.num_picker.toString();
        setting.setting_value = String.valueOf(numOfRecords);

        try {
            saveSetting(setting);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error saving");
            Log.e(LOG_TAG, setting.setting_name);
            e.printStackTrace();
        } finally {
            this.numOfRecords = numOfRecords;
        }

    }
}
