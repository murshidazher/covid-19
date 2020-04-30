package com.example.ncovidtracker.data;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Country.class}, version = 2, exportSchema = false)
abstract class CountryRoomDatabase extends RoomDatabase {


    abstract CountryDao countryDao();

    private static volatile CountryRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized CountryRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CountryRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CountryRoomDatabase.class, "country_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CountryDao mCountryDao;

        public PopulateDbAsyncTask(CountryRoomDatabase db) {
            this.mCountryDao = db.countryDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mCountryDao.insert(new Country("USA",
                    970757,
                    "10106",
                    "54941",
                    "685",
                    "118633",
                    "797183",
                    "15116",
                    "https://corona.lmao.ninja/assets/img/flags/us.png"));
            return null;
        }
    };
}
