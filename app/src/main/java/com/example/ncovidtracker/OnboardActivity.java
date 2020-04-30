package com.example.ncovidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardActivity extends AppCompatActivity {

    private ViewPager screenPager;
    OnboardViewPagerAdapter onboardViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext, btnGetStarted;
    LinearLayout linearLayoutNext, linearLayoutGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePreData()) {
            Intent loginActivity =  new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }

        setContentView(R.layout.activity_onboard);

        btnNext = findViewById(R.id.btn_onboard_next);
        btnGetStarted = findViewById(R.id.btn_onboard_getStarted);

        linearLayoutNext = findViewById(R.id.ll_onboard_next);
        linearLayoutGetStarted = findViewById(R.id.ll_onboard_getStarted);

        tabIndicator = findViewById(R.id.tab_indicator);

        // data
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Avoid close contact","Avoid close contants and maintain a social distance of about 6ft. Specially, maintain a safe distance from anyone who is coughing or sneezing.", R.drawable.ic_v_avoid_close_contact));
        mList.add(new ScreenItem("Clean your hands often","Use soap and water, or an alcohol-based hand rub. Wash your hands thoroughly for atleast 20 seconds.", R.drawable.ic_v_clean_hands));
        mList.add(new ScreenItem("Wear a facemask","Don’t touch your eyes, nose or mouth. Cover your nose and mouth with your bent elbow or a tissue when you cough or sneeze.", R.drawable.ic_v_wear_mask));

        // setup viewPager
        screenPager = findViewById(R.id.screen_viewpager);
        onboardViewPagerAdapter = new OnboardViewPagerAdapter(this, mList);
        screenPager.setAdapter(onboardViewPagerAdapter);

        // setup tab indicator
        tabIndicator.setupWithViewPager(screenPager);

        // button next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(screenPager.getCurrentItem()+1, true);
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // button get started
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();
            }
        });
    }

    private boolean restorePreData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        Boolean isOnboardActivityOpenBefore = preferences.getBoolean("isOnboardOpened", false);
//        return isOnboardActivityOpenBefore;
        return false;
    }

    private void savePrefsData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isOnboardOpened", true);
        editor.apply();
    }

    private void loadLastScreen() {
        linearLayoutNext.setVisibility(View.INVISIBLE);
        linearLayoutGetStarted.setVisibility(View.VISIBLE);
    }
}
