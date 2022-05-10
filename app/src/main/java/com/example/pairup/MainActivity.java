package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

/**
 * MainActivity: First Activity and Window when you open the App
 * Contains: Logo + Login Button + SignUp Button
 * For new App users or when user has logged out
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Initialize MainActivity. Creates a window where we can place our
     * Main Page UI with setContentView(view)
     * @param savedInstanceState: Bundle reference
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLocale();

        /*
          To keep User Logged In
          If LOGGED = true -> openPairUpActivity()
          If LOGGED = false -> .MainActivity
         */
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs.getBoolean("LOGGED", false)){
            openPairUpActivity();
        }

        // Retrieve with findViewById(int) the buttons from our UI to interact with
        // LOGIN   SIGNUP
        findViewById(R.id.button_main_login).setOnClickListener(view -> openLoginActivity());
        findViewById(R.id.button_main_signup).setOnClickListener(view -> openSignUpActivity());
    }

    /**
     * Open PairUpActivity when user was already logged
     */
    private void openPairUpActivity() {
        Intent intent = new Intent(this, PairUpActivity.class);
        startActivity(intent);
    }

    /**
     * Open LoginActivity when user clicks on Login Button
     */
    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        // Intent describes the activity to be executed
        startActivity(intent); // Start new activity
    }

    /**
     * Open SignUpActivity when user clicks SignUp Button
     */
    private void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public  void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("LANG", "");
        setLocale(language);
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.putString("LANG", lang);
        editor.apply();
    }
}