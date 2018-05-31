package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WordCharDAO {

    @Query("SELECT * from wordchars WHERE result_id = :resultId")
    List<WordCharEntity> getWords(int resultId);

    @Insert
    void insert(WordCharEntity wordEntity);

    @Query("DELETE from wordchars WHERE result_id = :resultId")
    void deleteWordChar(int resultId);

}
