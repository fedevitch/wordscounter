package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class SavedResultJoined {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public String id;

    public int words_count;

    public int chars_count;

    public String text;

    public Date created_at;

    @Relation(parentColumn = "id", entityColumn = "result_id", entity = WordEntity.class)
    public List<WordEntity> words;

    @Relation(parentColumn = "id", entityColumn = "result_id", entity = WordCharEntity.class)
    public List<WordCharEntity> characters;

}
