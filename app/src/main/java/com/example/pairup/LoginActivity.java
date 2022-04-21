package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private AppDatabase db;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getUserDatabase(getApplicationContext());

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);

        Button login = (Button) findViewById(R.id.login_button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validInput(email, password)){
                    // Query login
                    AppDatabase appDatabase = AppDatabase.getUserDatabase(getApplicationContext());
                    // Dao Initialization
                    UserDao userDao = db.userDao();

                    UserEntity userEntity = userDao.login(email.getText().toString(), password.getText().toString());
                    if (userEntity == null){
                        Toast.makeText(LoginActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                    }else{
                        openPairUpActivity();
                    }
                }else if (email.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(LoginActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();
                    openPairUpActivity();
                }else{
                    Toast.makeText(LoginActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openPairUpActivity(){
        Intent intent = new Intent(this, PairUpActivity.class);
        startActivity(intent);
    }

    public Boolean validInput(EditText gmail, EditText password) {

        if (gmail.getText().toString().isEmpty()) {
            email.setError("Gmail is empty");
            return false;
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Password is empty");
            return false;
        } else {
            email.setError(null);
            password.setError(null);
            return true;
        }
    }

}