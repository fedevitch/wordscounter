package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {

    private SettingsRepository settingsRepository;

    public SettingsViewModel(Application application) {

        super(application);
        settingsRepository = new SettingsRepository(application);

    }

    public List<SettingsEntity> getAllSettings() { return settingsRepository.getAllSettings(); }

    public void setDefaultSettings(List<SettingsEntity> settings) {
        settingsRepository.setDefaultSettings(settings);
    }

    public void updateSetting(SettingsEntity setting){
        settingsRepository.updateSetting(setting);
    }

}
