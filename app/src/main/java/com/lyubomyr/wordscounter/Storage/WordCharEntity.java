package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(tableName = "wordchars",
        foreignKeys = @ForeignKey(entity = SavedResultEntity.class,
            parentColumns = "id",
            childColumns = "result_id", onDelete = 1, onUpdate = 1))
public class WordCharEntity {

    @NonNull
    public int result_id;

    @NonNull
    public Character character;

    public int appears;

}
