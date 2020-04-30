package com.example.ncovidtracker.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CountryRepository {

    private CountryDao mCountryDao;
    private LiveData<List<Country>> mCountries;

    CountryRepository(Application application) {
        CountryRoomDatabase db = CountryRoomDatabase.getDatabase(application);
        mCountryDao = db.countryDao();
        mCountries = mCountryDao.getAscAlphabetizedCountry();
    }

    LiveData<List<Country>> getAllCountries() {
        return mCountries;
    }

    LiveData<List<Country>> getAlphabetizedCountry(boolean isAsc) {
        if(isAsc)
            return mCountryDao.getDescAlphabetizedCountry();
        else
            return mCountryDao.getAscAlphabetizedCountry();
    }

    LiveData<List<Country>> getSortedCasedCountry() {
        return mCountryDao.getSortTotalCasesCountry();
    }

    LiveData<List<Country>> getSearchedCountries(String searchQuery) {
        return mCountryDao.getCountryList(searchQuery);
    }

    public void insert(Country country) {
        new InsertCountryAsyncTask(mCountryDao).execute(country);
    }

    public void deleteAll() {
        CountryRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCountryDao.deleteAll();
        });
    }

    private static class InsertCountryAsyncTask extends AsyncTask<Country, Void, Void> {

        private CountryDao mCountryDao;

        public InsertCountryAsyncTask(CountryDao mCountryDao) {
            this.mCountryDao = mCountryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            mCountryDao.insert(countries[0]);
            return null;
        }
    }
}
