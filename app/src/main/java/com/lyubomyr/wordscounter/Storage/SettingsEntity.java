package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


@Entity(tableName = "settings")
public class SettingsEntity {

    @NonNull
    public String setting_type;

    @NonNull
    public String setting_value;

}
