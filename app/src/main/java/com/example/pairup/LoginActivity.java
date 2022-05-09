package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

/**
 * LoginActivity to enter into the app with current User Data
 */
public class LoginActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText gmail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        Display PairUp icon ToolBar
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */
        db = AppDatabase.getInstance(getApplicationContext());

        gmail = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        findViewById(R.id.login_button_login).setOnClickListener(view -> LoginUser());

    }

    /**
     * Enter into the application when user clicks Login
     * Check if User credentials are valid:
     *      - User Authorized: (User registered) Open PairUpActivity
     *      - Not authorized: (User credentials not in the DataBase) Inform with Toast Invalid Credentials
     */
    private void LoginUser(){
        if (validInput()){

            UserEntity userEntity = db.userDao().login(gmail.getText().toString(), password.getText().toString());

            if (userEntity == null){
                Toast.makeText(LoginActivity.this,getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
            }else{
                openPairUpActivity();
            }
        }
    }


    /**
     * Validate Gmail and Password fields:
     *      - Gmail not empty
     *      - Password not empty
     * @return true if all fields are filled
     *         false if at least one field is empty
     */
    private Boolean validInput() {
        boolean allFine = true;
        if (gmail.getText().toString().isEmpty()) {
            gmail.setError(getString(R.string.error_empty));
            allFine = false;
        } else{
            gmail.setError(null);
        }
        if (password.getText().toString().isEmpty()) {
            password.setError(getString(R.string.error_empty));
            allFine = false;
        } else {
            password.setError(null);
        }
        return allFine;
    }

    /**
     * Open PairUpActivity if Login credentials (gmail and password) are correct
     */
    private void openPairUpActivity(){
        // Save LOGGED = true, ID = current user id
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("LOGGED", true).apply();
        UserEntity current_user = db.userDao().getCurrentUser(gmail.getText().toString());
        prefs.edit().putInt("ID", (int)current_user.getId_user()).apply();

        Intent intent = new Intent(this, PairUpActivity.class);
        startActivity(intent);
        finish();
    }

}