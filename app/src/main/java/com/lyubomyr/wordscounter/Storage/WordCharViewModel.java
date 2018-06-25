package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

public class WordCharViewModel extends AndroidViewModel {

    private WordCharRepository wordCharRepository;

    public WordCharViewModel(Application application){
        super(application);
        wordCharRepository = new WordCharRepository(application);
    }

    public List<WordCharEntity> getWordChars(int resultId){
        return wordCharRepository.getAllWordChars(resultId);
    }

    public void saveWordChars(List<WordCharEntity> wordChars){
        wordCharRepository.saveWordChars(wordChars);
    }

    public void deleteWordChars(int resultId){
        wordCharRepository.deleteWordChars(resultId);
    }
}
