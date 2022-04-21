package com.example.pairup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.util.Log;

public class PairUpActivity extends AppCompatActivity {

    // Initialize all fragments
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    NotificationsFragment notificationFragment = new NotificationsFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    BottomNavigationView Nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_up);

        Nav = findViewById(R.id.bottom_navigation);

        // default Home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

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

                    case R.id.notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
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