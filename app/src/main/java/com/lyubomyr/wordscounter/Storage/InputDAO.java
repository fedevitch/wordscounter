package com.lyubomyr.wordscounter.Storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface InputDAO {

    @Insert
    void insertText(InputEntity inputEntity);

    @Query("UPDATE input_text SET text = :text WHERE id = :id")
    void updateText(InputEntity inputEntity);

    @Query("DELETE from input_text")
    void deleteSavedText();

    @Query("SELECT * from input_text LIMIT 1")
    InputEntity getSavedText();
}
