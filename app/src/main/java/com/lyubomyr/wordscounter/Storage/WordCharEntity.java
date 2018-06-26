package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "wordchars",
        foreignKeys = @ForeignKey(entity = SavedResultEntity.class,
            parentColumns = "id",
            childColumns = "result_id", onDelete = CASCADE, onUpdate = CASCADE))
public class WordCharEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    public String result_id;

    @NonNull
    public Character character;

    public int appears;

}
