package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SettingsDAO {

    @Query("SELECT * from settings")
    List<SettingsEntity> getAllSettings();

    @Query("UPDATE settings SET setting_value = :setting_value WHERE setting_name = :setting_name")
    void updateSetting(String setting_name, String setting_value);

    @Insert
    void setDefaultSettings(List<SettingsEntity> settings);

}
