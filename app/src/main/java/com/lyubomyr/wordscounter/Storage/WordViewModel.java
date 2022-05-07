package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository wordRepository;

    public WordViewModel(Application application){
        super(application);
        wordRepository = new WordRepository(application);
    }

    public List<WordEntity> getWords(int resultId) {
        return wordRepository.getWords(resultId);
    }

    public void saveWords(List<WordEntity> words){
        wordRepository.saveWords(words);
    }

    public void deleteWords(int resultId){
        wordRepository.deleteWords(resultId);
    }

}
