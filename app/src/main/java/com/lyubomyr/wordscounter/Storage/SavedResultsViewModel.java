package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SavedResultsViewModel extends AndroidViewModel {

    private SavedResultsRepository savedResultsRepository;
    private WordRepository wordRepository;
    private WordCharRepository wordCharRepository;

    public SavedResultsViewModel(Application application) {
        super(application);
        savedResultsRepository = new SavedResultsRepository(application);
        wordRepository = new WordRepository(application);
        wordCharRepository = new WordCharRepository(application);
    }

    public List<SavedResultJoined> getCountResults(){
        return savedResultsRepository.getCountResults();
    }

    public void saveCountResult(SavedResultJoined result){

        SavedResultEntity dbEntity = new SavedResultEntity();

        dbEntity.id = result.id;
        dbEntity.created_at = result.created_at;
        dbEntity.words_count = result.words_count;
        dbEntity.chars_count = result.chars_count;
        dbEntity.text = result.text;

        savedResultsRepository.saveResult(dbEntity);
        wordRepository.saveWords(result.words);
        wordCharRepository.saveWordChars(result.characters);
    }

    public void deleteSavedResult(String resultId){
        savedResultsRepository.deleteResult(resultId);
    }

    public void deleteAllSavedResults(){
        savedResultsRepository.deleteAllResults();
    }
}
