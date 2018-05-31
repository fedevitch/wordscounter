package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "words", foreignKeys = @ForeignKey(entity = SavedResultEntity.class, parentColumns = "id", childColumns = "result_id"))
public class WordEntity {

    @PrimaryKey
    public int id;

    @NonNull
    public int result_id;

    @NonNull
    public String word;

    public int appears;
}
