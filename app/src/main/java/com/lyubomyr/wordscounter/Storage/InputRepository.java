package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Date;

public class InputRepository {

    private final InputDAO inputDAO;

    private final InputEntity inputEntity;

    InputRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        inputDAO = db.inputDAO();
        inputEntity = GetSavedText();

        if(inputEntity.id == 0) {
            inputEntity.created_at = new Date();
            inputEntity.modified_at = new Date();
            inputEntity.text = "";

            new initSavedTextDataAsync(inputDAO).doInBackground(inputEntity);
        }
    }

    InputEntity GetSavedText() {
        return new getSavedTextAsync(inputDAO).doInBackground();
    }

    private static class getSavedTextAsync extends AsyncTask<Void, Void, InputEntity> {
        InputDAO asyncDao;

        getSavedTextAsync(InputDAO dao) { asyncDao = dao; }

        @Override
        protected InputEntity doInBackground(final Void... params) {
            return asyncDao.getSavedText();
        }
    }

    private static class initSavedTextDataAsync extends AsyncTask<InputEntity, Void, Void> {
        InputDAO asyncDao;

        initSavedTextDataAsync(InputDAO dao) { asyncDao = dao; }

        @Override
        protected Void doInBackground(final InputEntity... params) {
            asyncDao.insertText(params[0]);
            return null;
        }
    }

    public void SaveText(String text) {
        inputEntity.text = text;
        inputEntity.modified_at = new Date();
        new saveTextAsync(inputDAO).execute(inputEntity);
    }

    private static class saveTextAsync extends AsyncTask<InputEntity, Void, Void> {
        InputDAO asyncDao;

        saveTextAsync(InputDAO dao) { asyncDao = dao; }

        @Override
        protected Void doInBackground(final InputEntity... params) {
            InputEntity entity = params[0];
            asyncDao.updateText(entity.text, entity.modified_at, entity.id);
            return null;
        }
    }


    public void deleteSavedText(){
        new deleteSavedTextAsync(inputDAO).doInBackground();
    }

    private static class deleteSavedTextAsync extends AsyncTask<Void, Void, Void> {
        InputDAO asyncDao;

        deleteSavedTextAsync(InputDAO dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params){
            asyncDao.deleteSavedText();
            return null;
        }
    }

}
