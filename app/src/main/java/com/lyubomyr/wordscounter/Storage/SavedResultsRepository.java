package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class SavedResultsRepository {

    private final SavedResultDAO savedResultDAO;

    private final LiveData<List<SavedResultJoined>> mAllCountResults;

    SavedResultsRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        savedResultDAO = db.savedResultDAO();

        mAllCountResults = savedResultDAO.getSavedResults();
    }

    LiveData<List<SavedResultJoined>> getAllCountResults(){
        return mAllCountResults;
    }

    public List<SavedResultJoined> getCountResults(){

        return new getCountResultsAsync(savedResultDAO).doInBackground();

    }

    private static class getCountResultsAsync extends AsyncTask<Void, Void, List<SavedResultJoined>> {
        SavedResultDAO asyncDao;

        getCountResultsAsync(SavedResultDAO dao){
            asyncDao = dao;
        }

        @Override
        protected List<SavedResultJoined> doInBackground(final Void... params){
            return asyncDao.getAllSavedResults();
        }
    }

    public void saveResult(SavedResultEntity result){
        new saveResultAsync(savedResultDAO).execute(result);
    }

    private static class saveResultAsync extends AsyncTask<SavedResultEntity, Void, Void>{
        SavedResultDAO asyncDao;

        saveResultAsync(SavedResultDAO dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(final SavedResultEntity... params){
            asyncDao.insertResult(params[0]);
            return null;
        }
    }

    public void deleteResult(String resultId){
        new deleteResultAsync(savedResultDAO).execute(resultId);
    }

    private static class deleteResultAsync extends AsyncTask<String, Void, Void> {
        SavedResultDAO asyncDao;

        deleteResultAsync(SavedResultDAO dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params){
            asyncDao.deleteResult(params[0]);
            return null;
        }
    }

    public void deleteAllResults(){
        new deleteAllResultsAsync(savedResultDAO).doInBackground();
    }

    private static class deleteAllResultsAsync extends AsyncTask<Void, Void, Void> {
        SavedResultDAO asyncDao;

        deleteAllResultsAsync(SavedResultDAO dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params){
            asyncDao.deleteAll();
            return null;
        }
    }


}
