package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class WordRepository {

    private WordDAO wordDAO;

    WordRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        wordDAO = db.wordDAO();
    }

    public List<WordEntity> getWords(int resultId){
        return new getWordsAsync(wordDAO).doInBackground(resultId);
    }

    private static class getWordsAsync extends AsyncTask<Integer, Void, List<WordEntity>> {
        private WordDAO asyncWordDao;

        getWordsAsync(WordDAO dao){
            asyncWordDao = dao;
        }

        @Override
        protected List<WordEntity> doInBackground(final Integer... params){
            return asyncWordDao.getWords(params[0]);
        }
    }

    public void saveWords(List<WordEntity> words){
        new saveWordsAsync(wordDAO).execute(words);
    }

    private static class saveWordsAsync extends AsyncTask<List<WordEntity>, Void, Void> {
        private WordDAO asyncWordDao;

        saveWordsAsync(WordDAO dao){
            asyncWordDao = dao;
        }

        @Override
        protected Void doInBackground(final List<WordEntity>... params){
            asyncWordDao.insertWords(params[0]);
            return null;
        }
    }

    public void deleteWords(int resultId){
        new deleteWordsAsync(wordDAO).execute(resultId);
    }

    private static class deleteWordsAsync extends AsyncTask<Integer, Void, Void> {
        private WordDAO asyncWordDao;

        deleteWordsAsync(WordDAO dao){
            asyncWordDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params){
            asyncWordDao.deleteWords(params[0]);
            return null;
        }
    }
}
