package com.example.pairup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;


/**
 * SignUp Activity for Registering new users.
 * Contains EditText fields to fill with new user information
 * All fields must be correctly filled in order to Register new User
 */
public class SignUpActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText gmail, password, re_password, name;

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

    /**
     * Called when user clicks SignUp Button
     * Creates new user Entity
     */
    public void RegisterUser(){
        if (validateInput()) {
            UserEntity user = new UserEntity(); // Initialize new User Entity

            user.setGmail(gmail.getText().toString());
            user.setPassword(password.getText().toString());
            user.setName(name.getText().toString());

            db.userDao().registerUser(user);

            openPairUpActivity(user.getGmail());
        }
    }

    /**
     * Open PairUpActivity when user clicks button SignUp and all fields are validated
     * @param gmail: pass user gmail to PairUpActivity to retrieve user data
     */
    public void openPairUpActivity(@Nullable String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);  // Pass gmail to the new activity
        startActivity(intent);
    }

    /**
     * Validate all fields are correctly filled
     * One by one because all errors appear when user clicks SignUp
     * @return: true if all fields are correctly filled
     *          false if at least one field is wrong
     */
    public Boolean validateInput(){
        // To show all errors at the same time
        boolean allFine = true;
        if (!validateName()){
            allFine = false;
        }
        if (!validatePassword()){
            allFine = false;
        }
        if (!validateRePassword()){
            allFine = false;
        }
        if (!validateGmail()){
            allFine = false;
        }
        return allFine;
    }

    /**
     * Checks if Username field is correctly filled:
     *      - Not empty
     *      - Not too long (length < 15)
     *      - Not too short (length > 4)
     * @return: true if Username field correctly filled
     *          false otherwise
     */
    public Boolean validateName(){
        String string_name = name.getText().toString();
        if (string_name.isEmpty()){
            name.setError("Cannot be empty");
            return false;
        } else if (string_name.length() >= 15 ){
            name.setError("Username too long");
            return false;
        } else if (string_name.length() < 4){
            name.setError("Username too short");
            return false;
        } else{
            name.setError(null);
            return true;
        }
    }

    /**
     * Checks if Password field is correctly filled:
     *      - Not empty
     *      - Not too short (length >= 8)
     * @return: true if Password field correctly filled
     *          false otherwise
     */
    public Boolean validatePassword(){
        // To Do: Stronger password
        String string_password = password.getText().toString();
        if (string_password.isEmpty()){
            password.setError("Cannot be empty");
            return false;
        }
        /*else if (string_password.length() < 8){
            password.setError("Password too short");
            return false;
        } */
        else{
            password.setError(null);
            return true;
        }
    }

    /**
     * Checks if RepeatPassword field is correctly filled::
     *      - Not empty
     *      - Match password field
     * @return: true if RepeatPassword field correctly filled
     *          false otherwise
     */
    public Boolean validateRePassword(){
        String string_password = password.getText().toString();
        String string_re_password = re_password.getText().toString();

        if (string_re_password.isEmpty()){
            re_password.setError("Repeat Password");
            return false;
        } else if (!string_re_password.equals(string_password)){
            re_password.setError("Passwords do not match");
            return false;
        } else{
            re_password.setError(null);
            return true;
        }
    }

    /**
     * Checks if Gmail field is correctly filled:
     *      - Not empty
     *      - EMAIL_ADDRESS valid format
     *      - Not already registered with that email address
     *
     * @return: true if Gmail field correctly filled
     *          false otherwise
     */
    public Boolean validateGmail(){
        String string_gmail = gmail.getText().toString();
        if (string_gmail.isEmpty()){
            gmail.setError("Cannot be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(string_gmail).matches()){
            gmail.setError("Invalid Email");
            return false;
        } else if (db.userDao().getCurrentUser(string_gmail) != null){
            gmail.setError("User Email already registered");
            return false;
        } else{
            gmail.setError(null);
            return true;
        }
    }


}