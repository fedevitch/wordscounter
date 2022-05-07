package com.lyubomyr.wordscounter.Storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {
        SettingsEntity.class, SavedResultEntity.class,
        WordEntity.class, WordCharEntity.class,
        InputEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract SavedResultDAO savedResultDAO();

    public abstract WordDAO wordDAO();

    public abstract WordCharDAO wordCharDAO();

    public abstract SettingsDAO settingsDAO();

    public abstract InputDAO inputDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "words_counter_db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return INSTANCE;
    }

}
