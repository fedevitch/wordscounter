package com.lyubomyr.wordscounter.Storage;

import android.app.Application;
import android.os.AsyncTask;

public class InputRepository {

    private final InputDAO inputDAO;

    InputRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        inputDAO = db.inputDAO();
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

}
