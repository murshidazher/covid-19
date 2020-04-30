package com.example.ncovidtracker.ui.country;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ncovidtracker.R;
import com.example.ncovidtracker.data.Country;

public class CovidCountryDetail extends AppCompatActivity {

    TextView tvDetailCountryName, tvDetailTotalCases, tvDetailTodayCases, tvDetailTotalDeaths,
            tvDetailTodayDeaths, tvDetailTotalRecovered, tvDetailTotalActive, tvDetailTotalCritical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_country_detail);


        tvDetailCountryName = findViewById(R.id.tvDetailCountryName);
        tvDetailTotalCases = findViewById(R.id.tvDetailTotalCases);
        tvDetailTodayCases = findViewById(R.id.tvDetailTodayCases);
        tvDetailTotalDeaths = findViewById(R.id.tvDetailTotalDeaths);
        tvDetailTodayDeaths = findViewById(R.id.tvDetailTodayDeaths);
        tvDetailTotalRecovered = findViewById(R.id.tvDetailTotalRecovered);
        tvDetailTotalActive = findViewById(R.id.tvDetailTotalActive);
        tvDetailTotalCritical = findViewById(R.id.tvDetailTotalCritical);



        Country covidCountry = getIntent().getParcelableExtra("EXTRA_COVID");


        tvDetailCountryName.setText(covidCountry.getCountry());
        tvDetailTotalCases.setText(Integer.toString(covidCountry.getCases()));
        tvDetailTodayCases.setText(covidCountry.getTodayCases());
        tvDetailTotalDeaths.setText(covidCountry.getDeaths());
        tvDetailTodayDeaths.setText(covidCountry.getTodayDeaths());
        tvDetailTotalRecovered.setText(covidCountry.getRecovered());
        tvDetailTotalActive.setText(covidCountry.getActive());
        tvDetailTotalCritical.setText(covidCountry.getCritical());

    }
}
