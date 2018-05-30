package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SavedResultDAO {

    @Insert
    void insert(SavedResultEntity savedResultEntity);

    @Query("DELETE from saved_results")
    void deleteAll();

    @Query("SELECT * from saved_results ORDER BY created_at DESC")
    List<SavedResultEntity> getAllResultsInfo();

}
