package com.example.ncovidtracker.data;

import android.app.Application;
import android.util.Log;

import java.util.List;
import java.util.*;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CountryViewModel extends AndroidViewModel {
    private CountryRepository mRepository;
    private LiveData<List<Country>> mAllCountries;

    public CountryViewModel(Application application) {
        super(application);
        mRepository = new CountryRepository(application);
        mAllCountries = mRepository.getAllCountries();
    }

    public LiveData<List<Country>> getmAllCountries() {
        return mAllCountries;
    }

    public LiveData<List<Country>> getAlphabetSortedCountries(boolean isAsc) {
        return mRepository.getAlphabetizedCountry(isAsc);
    }

    public LiveData<List<Country>> getCaseSortedCountries() {
        return mRepository.getSortedCasedCountry();
    }

    public LiveData<List<Country>> getSearchedCountries(String searchQuery) {
        return mRepository.getSearchedCountries(searchQuery);
    }

    public void insert(Country country) {
        mRepository.insert(country);
    }

    public void insertAll(List<Country> countries) {
        for (Country country : countries) {
            insert(country);
        }
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
