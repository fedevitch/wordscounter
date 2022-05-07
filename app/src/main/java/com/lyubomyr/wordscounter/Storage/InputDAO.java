package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;

@Dao
public interface InputDAO {

    @Insert
    void insertText(InputEntity inputEntity);

    @Query("UPDATE input_text SET text = :text, modified_at = :modified_at WHERE id = :id")
    void updateText(String text, Date modified_at, int id);

    @Query("DELETE from input_text")
    void deleteSavedText();

    @Query("SELECT * from input_text LIMIT 1")
    InputEntity getSavedText();
}
