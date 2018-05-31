package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "settings")
public class SettingsEntity {

    @PrimaryKey
    @NonNull
    public String setting_name;

    @NonNull
    public String setting_type;

    @NonNull
    public String setting_value;

}
