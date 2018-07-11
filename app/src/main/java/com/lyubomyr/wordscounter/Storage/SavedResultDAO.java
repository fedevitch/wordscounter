package com.lyubomyr.wordscounter.Storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SavedResultDAO {

    @Insert
    void insertResult(SavedResultEntity savedResultEntity);

    @Query("DELETE from saved_results")
    void deleteAll();

    @Query("DELETE from saved_results WHERE id = :resultId")
    void deleteResult(String resultId);

    @Query("SELECT * from saved_results ORDER BY created_at DESC")
    List<SavedResultJoined> getAllSavedResults();

    @Query("SELECT * from saved_results ORDER BY created_at DESC")
    LiveData<List<SavedResultJoined>> getSavedResults();

}
