package com.example.pairup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserDao;
import com.example.pairup.db.UserEntity;

public class LoginActivity extends AppCompatActivity {

    private AppDatabase db;
    EditText gmail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(getApplicationContext());

        gmail = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.login_button_login).setOnClickListener(view -> LoginUser());

    }

    public void LoginUser(){
        if (validInput()){

            UserEntity userEntity = db.userDao().login(gmail.getText().toString(), password.getText().toString());
            if (userEntity == null){
                Toast.makeText(LoginActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
            }else{
                openPairUpActivity(gmail.getText().toString());
            }

        }else{
            Toast.makeText(LoginActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
        }
    }

    public void openPairUpActivity(@Nullable String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);
        startActivity(intent);
    }

    public Boolean validInput() {
        boolean allFine = true;
        if (gmail.getText().toString().isEmpty()) {
            gmail.setError("Gmail is empty");
            allFine = false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is empty");
            allFine = false;
        } else {
            gmail.setError(null);
            password.setError(null);
        }
        return allFine;
    }


}