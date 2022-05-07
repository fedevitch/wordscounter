package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "input_text")
public class InputEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String text;

    public Date created_at;

    public Date modified_at;
}
