package com.example.ncovidtracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ncovidtracker.ui.country.CountryFragment;
import com.example.ncovidtracker.ui.home.HomeFragment;
import com.example.ncovidtracker.ui.info.InfoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.nav_view);



        if(savedInstanceState == null) {
            bottomNav.setItemSelected(R.id.navigation_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, homeFragment)
                    .commit();
        }



        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.navigation_country:
                        fragment = new CountryFragment();
                        break;
                    case R.id.navigation_info:
                        fragment = new InfoFragment();
                        break;
                }

                if(fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "error in creating fragment");
                }

            }
        });

    }

}
