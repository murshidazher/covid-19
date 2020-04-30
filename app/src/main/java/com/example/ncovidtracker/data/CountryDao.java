package com.example.ncovidtracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * from country_table ORDER BY country ASC")
    LiveData<List<Country>> getAscAlphabetizedCountry();

    @Query("SELECT * from country_table ORDER BY country DESC")
    LiveData<List<Country>> getDescAlphabetizedCountry();

    @Query("SELECT * from country_table ORDER BY cases DESC")
    LiveData<List<Country>> getSortTotalCasesCountry();

    @Query("SELECT * from country_table WHERE country LIKE '%' || :countryText || '%' ORDER BY cases DESC")
    LiveData<List<Country>> getCountryList(String countryText);

    @Query("SELECT * from country_table WHERE country LIKE :countryText")
    LiveData<Country> getCountry(String countryText);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Country country);

    @Query("DELETE FROM country_table")
    void deleteAll();
}
