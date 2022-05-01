package com.example.pairup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    public void LoginUser(){
        if (validInput()){

            UserEntity userEntity = db.userDao().login(gmail.getText().toString(), password.getText().toString());

            if (userEntity == null){
                Toast.makeText(LoginActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
            }else{
                openPairUpActivity(gmail.getText().toString());
            }
        }
    }

    /**
     * Open PairUpActivity if Login credentials (gmail and password) are correct
     * @param gmail: pass user gmail to PairUpActivity to retrieve user data
     */
    public void openPairUpActivity(@Nullable String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);
        startActivity(intent);
    }

    /**
     * Validate Gmail and Password fields:
     *      - Gmail not empty
     *      - Password not empty
     * @return true if all fields are filled
     *         false if at least one field is empty
     */
    public Boolean validInput() {
        boolean allFine = true;
        if (gmail.getText().toString().isEmpty()) {
            gmail.setError("Gmail is empty");
            allFine = false;
        } else{
            gmail.setError(null);
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is empty");
            allFine = false;
        } else {
            password.setError(null);
        }
        return allFine;
    }


}