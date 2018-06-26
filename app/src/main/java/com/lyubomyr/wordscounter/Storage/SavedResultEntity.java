package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

@Entity(tableName = "saved_results")
public class SavedResultEntity {

    @PrimaryKey
    @NonNull
    public String id;

    public int words_count;

    public int chars_count;

    public String text;

    public Date created_at;

}
