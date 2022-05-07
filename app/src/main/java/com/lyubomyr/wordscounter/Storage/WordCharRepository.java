package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class WordCharRepository {

    private WordCharDAO wordCharDAO;

    WordCharRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        wordCharDAO = db.wordCharDAO();
    }

    public List<WordCharEntity> getAllWordChars(int resultId){
        return new getAllWordCharsAsync(wordCharDAO).doInBackground(resultId);
    }

    private static class getAllWordCharsAsync extends AsyncTask<Integer, Void, List<WordCharEntity>> {
        private WordCharDAO asyncDao;

        getAllWordCharsAsync(WordCharDAO dao){
            asyncDao = dao;
        }

        @Override
        protected List<WordCharEntity> doInBackground(final Integer... params){
            return asyncDao.getWords(params[0]);
        }
    }

    public void saveWordChars(List<WordCharEntity> wordChars){
        new saveWordCharsAsync(wordCharDAO).execute(wordChars);
    }

    private static class saveWordCharsAsync extends AsyncTask<List<WordCharEntity>, Void, Void>{
        private WordCharDAO asyncDao;

        saveWordCharsAsync(WordCharDAO dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(final List<WordCharEntity>... params){
            asyncDao.insertWordChars(params[0]);
            return null;
        }
    }

    public void deleteWordChars(int resultId){
        new deleteWordcharsAsync(wordCharDAO).execute(resultId);
    }

    private static class deleteWordcharsAsync extends AsyncTask<Integer, Void, Void> {
        private WordCharDAO asyncDao;

        deleteWordcharsAsync(WordCharDAO dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params){
            asyncDao.deleteWordChar(params[0]);
            return null;
        }
    }

}
