package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WordDAO {

    @Query("SELECT * from words WHERE result_id = :resultId")
    List<WordEntity> getWords(int resultId);

    @Insert
    void insertWords(List<WordEntity> wordEntity);

    @Query("DELETE from words WHERE result_id = :resultId")
    void deleteWords(int resultId);

}
