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

public class SignUpActivity extends AppCompatActivity {

    private AppDatabase db;

    EditText gmail, password, re_password, name;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = AppDatabase.getInstance(getApplicationContext());

        name = findViewById(R.id.signup_username);
        gmail = findViewById(R.id.signup_gmail);
        password = findViewById(R.id.signup_password);
        re_password = findViewById(R.id.signup_repeat_password);

        findViewById(R.id.signup_button_signup).setOnClickListener(view -> RegisterUser());
    }

    public void RegisterUser(){
        UserEntity user = new UserEntity();
        // Initialize User Entity
        user.setGmail(gmail.getText().toString());
        user.setPassword(password.getText().toString());
        user.setName(name.getText().toString());

        if (validateInput(user)) {

            db.userDao().registerUser(user);

            openPairUpActivity(user.getGmail());

            finish();
        }
    }

    public void openPairUpActivity(@Nullable String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);
        startActivity(intent);
    }


    private Boolean validateInput(UserEntity userEntity){
        // To show all errors at the same time
        boolean allFine = true;
        if (!validateName(userEntity)){
            allFine = false;
        }
        if (!validatePassword(userEntity)){
            allFine = false;
        }
        if (!validateRePassword(userEntity)){
            allFine = false;
        }
        if (!validateGmail(userEntity)){
            allFine = false;
        }
        return allFine;
    }

    private Boolean validateName(UserEntity userEntity){
        if (userEntity.getName().isEmpty()){
            name.setError("Cannot be empty");
            return false;
        } else if (userEntity.getName().length() >= 15 ){
            name.setError("Username too long");
            return false;
        } else if (userEntity.getName().length() < 4){
            name.setError("Username too short");
            return false;
        }
        else{
            name.setError(null); // next time user enters name
            return true;
        }
    }

    private Boolean validatePassword(UserEntity userEntity){
        // To Do: Strong password
        if (userEntity.getPassword().isEmpty()){
            password.setError("Cannot be empty");
            return false;
        }
        else{
            password.setError(null); // next time user enters name
            return true;
        }
    }

    private Boolean validateRePassword(UserEntity userEntity){
        String check = re_password.getText().toString();
        String coincide = userEntity.getPassword().toString();

        if (check.matches("")){
            re_password.setError("Repeat Password");
            return false;
        }
        else if (!check.equals(coincide)){
            re_password.setError("Password not coincide");
            return false;
        }else{
            re_password.setError(null); // next time user enters name
            return true;
        }
    }

    private Boolean validateGmail(UserEntity userEntity){
        if (userEntity.getGmail().isEmpty()){
            gmail.setError("Cannot be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEntity.getGmail()).matches()){
            gmail.setError("Invalid Email");
            return false;
        }
        else{
            gmail.setError(null); // next time user enters name
            return true;
        }
    }


}