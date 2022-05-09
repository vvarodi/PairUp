package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * PairUpActivity holds the main Fragments of our App, where you can navigate with the bottom navigation view:
 *      SettingsFragment
 *      HomeFragment
 *      ProfileFragment
 */
public class PairUpActivity extends AppCompatActivity {

    // Initialize all fragments
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    BottomNavigationView Nav;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_up);

        // Display PairUp icon ToolBar
        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_logo);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // default Home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        Nav = findViewById(R.id.bottom_navigation);
        Nav.setSelectedItemId(R.id.home);
        Nav.setOnItemSelectedListener(item -> {
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
        });

    }

    /**
     * Customize Back Button depending on which is the current fragment
     * If HomeFragment: Go out / finish
     * Otherwise go to HomeFragment
     */
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

    /**
     * Change Action Bar Tittle from fragment
     * https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
     * @param title: new action bar title
     */
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    public String getCurrentUser(){
        String gmail = getIntent().getStringExtra("GMAIL");
        return gmail;
    }
}