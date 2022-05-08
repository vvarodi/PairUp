package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class PairUpActivity extends AppCompatActivity {

    // Initialize all fragments
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    BottomNavigationView Nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_up);

        // Display PairUp icon ToolBar
        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_logo);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Nav = findViewById(R.id.bottom_navigation);
        Nav.setSelectedItemId(R.id.home);
        // default Home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        SharedPreferences mPrefs = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        String gmail = getIntent().getStringExtra("GMAIL");
        editor.putString("EMAIL", gmail);
        editor.apply();


        Nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;

                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }
    @Override
    public void onBackPressed(){
        if (Nav.getSelectedItemId() == R.id.home){
            //super.onBackPressed();
            finishAffinity();
            finish();
        }else{
            Nav.setSelectedItemId(R.id.home);
        }
    }

    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    public String getCurrentUser(){
        String gmail = getIntent().getStringExtra("GMAIL");
        return gmail;
    }
}