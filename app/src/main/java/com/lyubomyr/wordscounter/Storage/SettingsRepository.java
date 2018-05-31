package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class SettingsRepository {

    private SettingsDAO settingsDAO;
    private List<SettingsEntity> settingsEntities;

    SettingsRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        settingsDAO = db.settingsDAO();
        //settingsEntities = settingsDAO.getAllSettings();
    }

    public List<SettingsEntity> getAllSettings(){
        return new getAllSettingsAsync(settingsDAO).doInBackground();
    }

    private static class getAllSettingsAsync extends AsyncTask<Void, Void, List<SettingsEntity>> {
        private SettingsDAO asyncSettingsDAO;

        getAllSettingsAsync(SettingsDAO dao) {
            asyncSettingsDAO = dao;
        }

        @Override
        protected List<SettingsEntity> doInBackground(Void... params){
            return asyncSettingsDAO.getAllSettings();
        }
    }

    public void updateSetting(SettingsEntity setting){
        new updateSettingAsync(settingsDAO).execute(setting);
    }

    private static class updateSettingAsync extends AsyncTask<SettingsEntity, Void, Void> {
        private SettingsDAO asyncSettingsDAO;

        updateSettingAsync(SettingsDAO dao) {
            asyncSettingsDAO = dao;
        }

        @Override
        protected Void doInBackground(final SettingsEntity... params) {
            asyncSettingsDAO.updateSetting(params[0].setting_name, params[0].setting_value);
            return null;
        }
    }

    public void setDefaultSettings(List<SettingsEntity> settings) {
        new setDefaultSettingsAsync(settingsDAO).execute(settings);
    }

    private static class setDefaultSettingsAsync extends AsyncTask<List<SettingsEntity>, Void, Void> {
        private SettingsDAO asyncSettingsDAO;

        setDefaultSettingsAsync(SettingsDAO settingsDao){
            asyncSettingsDAO = settingsDao;
        }

        @Override
        protected Void doInBackground(final List<SettingsEntity>... params) {
            asyncSettingsDAO.setDefaultSettings(params[0]);
            return null;
        }
    }

}
