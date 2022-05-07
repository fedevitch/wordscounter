package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class InputViewModel extends AndroidViewModel {

    private final InputRepository inputRepository;

    public InputViewModel(@NonNull Application application) {
        super(application);
        inputRepository = new InputRepository(application);
    }

    public String GetSavedText() {
        return inputRepository.GetSavedText().text;
    }

    public void SaveText(String text) {
        inputRepository.SaveText(text);
    }

    public void ClearInput() {
        inputRepository.deleteSavedText();
    }
}
