package com.example.ncovidtracker.ui.country;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ncovidtracker.R;
import com.example.ncovidtracker.data.Country;
import com.example.ncovidtracker.data.CountryListAdapter;
import com.example.ncovidtracker.data.CountryViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CountryFragment extends Fragment {


    public static final int NEW_COUNTRY_ACTIVITY_REQUEST_CODE = 1;

    private TextView tvTotalConfirmed, tvTotalDeaths, tvTotalRecovered, tvTotalActive, tvTotalCritical, tvLastUpdated, tvTotalCountries;

    private ImageView btnRefresh;

    RecyclerView rvCovidCountry;
    ProgressBar progressBar;

    private CountryViewModel mCountryViewModel;

    private static final String TAG = CountryFragment.class.getSimpleName();

    List<Country> countryList;
    CountryListAdapter countryAdapter;
    private boolean isSortedByAlphabetAsc = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country, container, false);

        // set has option menu as true because we have menu
        setHasOptionsMenu(true);



        // set main stats
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = root.findViewById(R.id.tvTotalDeaths);
        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
        tvTotalActive = root.findViewById(R.id.tvTotalActive);
        tvTotalCritical = root.findViewById(R.id.tvTotalCritical);
        tvLastUpdated = root.findViewById(R.id.tvLastUpdated);
        tvTotalCountries = root.findViewById(R.id.tvTotalCountries);
        btnRefresh = root.findViewById(R.id.btn_refresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                getDataFromServerSortTotalCases();
            }
        });


        // call view
        rvCovidCountry = root.findViewById(R.id.rvCovidCountry);
        progressBar = root.findViewById(R.id.progress_circular_country);
        rvCovidCountry.setLayoutManager(new LinearLayoutManager(getActivity()));



        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCovidCountry.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        rvCovidCountry.addItemDecoration(dividerItemDecoration);


        //call list
//        covidCountries = new ArrayList<>();


        // get basic stats
        getData();

        // call Volley method
//        getDataFromServerSortTotalCases();


        // added room database caching

        // Get a new or existing ViewModel from the ViewModelProvider.
        mCountryViewModel = new ViewModelProvider(this).get(CountryViewModel.class);

        countryList = new ArrayList<>();

        getDataFromServerSortTotalCases();

        // end room database caching

        return root;
    }

    private void showRecylerViewRoom() {
        countryAdapter = new CountryListAdapter(getActivity());
        rvCovidCountry.setAdapter(countryAdapter);

        ItemClickSupport.addTo(rvCovidCountry).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedCovidCountry(countryList.get(position));
            }
        });
    }

    private void sortedAlphabetizedCountry() {
        mCountryViewModel.getAlphabetSortedCountries(isSortedByAlphabetAsc).observe(getViewLifecycleOwner(), new Observer<List<Country>>() {
            @Override
            public void onChanged(@Nullable final List<Country> countries) {
                countryList = countries;
                countryAdapter.setCountries(countries);
                tvTotalCountries.setText(countryAdapter.getItemCount() + " countries");
                progressBar.setVisibility(View.GONE);
            }

        });

        isSortedByAlphabetAsc = !isSortedByAlphabetAsc;
    }

    private void sortedCasesCountry() {
        mCountryViewModel.getCaseSortedCountries().observe(getViewLifecycleOwner(), new Observer<List<Country>>() {
            @Override
            public void onChanged(@Nullable final List<Country> countries) {
                countryList = countries;
                countryAdapter.setCountries(countries);
                tvTotalCountries.setText(countryAdapter.getItemCount() + " countries");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getFilteredCountries(String searchQuery) {
        mCountryViewModel.getSearchedCountries(searchQuery).observe(getViewLifecycleOwner(), new Observer<List<Country>>() {
            @Override
            public void onChanged(@Nullable final List<Country> countries) {
                countryAdapter.setCountries(countries);
                tvTotalCountries.setText(countryAdapter.getItemCount() + " countries");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showSelectedCovidCountry(Country country) {
        Intent covidCovidCountryDetail = new Intent(getActivity(), CovidCountryDetail.class);
        covidCovidCountryDetail.putExtra("EXTRA_COVID", country);
        startActivity(covidCovidCountryDetail);
    }

    private void getDataFromServerSortTotalCases() {
        String url = "https://corona.lmao.ninja/v2/countries";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            // Extract JSONObject inside JSONObject
                            JSONObject countryInfo = data.getJSONObject("countryInfo");

                            countryList.add(new Country(
                                    data.getString("country"), data.getInt("cases"),
                                    data.getString("todayCases"), data.getString("deaths"),
                                    data.getString("todayDeaths"), data.getString("recovered"),
                                    data.getString("active"), data.getString("critical"),
                                    countryInfo.getString("flag")
                            ));

                        }


                        mCountryViewModel.insertAll(countryList);

                        tvTotalCountries.setText(jsonArray.length() + " countries");

                        showRecylerViewRoom();
                        sortedCasesCountry();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: " + error);
                    }
                });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.country_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint("Search...");
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (countryAdapter != null) {
                    getFilteredCountries(newText);
                }
                return true;
            }
        });

        searchItem.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_alpha:
                String order = (isSortedByAlphabetAsc)? "DESC" : "ASC";
                Toast.makeText(getContext(), "Sort Alphabetically " + order, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                sortedAlphabetizedCountry();
                return true;

            case R.id.action_sort_cases:
                Toast.makeText(getContext(), "Sort by Total Cases", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                sortedCasesCountry();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String getDate(long milliSecond){
        // Mon, 23 Mar 2020 02:01:04 PM
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yy hh:ss aaa");

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://corona.lmao.ninja/v2/all";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    tvTotalConfirmed.setText(jsonObject.getString("cases"));
                    tvTotalDeaths.setText(jsonObject.getString("deaths"));
                    tvTotalRecovered.setText(jsonObject.getString("recovered"));
                    tvTotalActive.setText(jsonObject.getString("active"));
                    tvTotalCritical.setText(jsonObject.getString("critical"));
                    tvLastUpdated.setText(getDate(jsonObject.getLong("updated")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error Response", error.toString());
            }
        });

        queue.add(stringRequest);
    }
}
