package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * MainActivity: First Activity and Window when you open the App
 * Contains: Logo + Login Button + SignUp Button
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

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs.getBoolean("LOGGED", false)){
            openPairUpActivity();
        }

        // Retrieve with findViewById(int) the buttons from our UI to interact with
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

}