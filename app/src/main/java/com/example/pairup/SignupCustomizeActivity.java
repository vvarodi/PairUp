package com.example.pairup;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

public class SignupCustomizeActivity extends AppCompatActivity {

    private AppDatabase db;

    EditText biography, languages;
    Button save, skip, openDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupcustomize);

        db = AppDatabase.getInstance(getApplicationContext());

        String email = getIntent().getExtras().getString("GMAIL");
        //
        // openDialog = findViewById(R.id.choose_color);
        // openDialog.setOnClickListener((view -> {languagePicker();}));



        //
        biography = findViewById(R.id.customize_biography);
        languages = findViewById(R.id.customize_languages);

        save = findViewById(R.id.customize_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.userDao().updateBiography(email, biography.getText().toString());
                db.userDao().updateLanguages(email, languages.getText().toString());

                UserEntity user = db.userDao().getCurrentUser(email);

                if (validBiography(user)) {
                    openPairUpActivity(user.getGmail());

                    Toast.makeText(SignupCustomizeActivity.this, "PROFILE CUSTOMIZATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skip = findViewById(R.id.customize_skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserEntity user = db.userDao().getCurrentUser(email);

                openPairUpActivity(user.getGmail());
            }
        });
    }

    /*public void languagePicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // languages = findViewById(R.id.customize_languages);

        // String email = getIntent().getExtras().getString("GMAIL");

        // UserEntity user = db.userDao().getCurrentUser(email);

        String[] languages = {"Italian","French","English","Spanish","Portuguese","Japanese","Mandarin","Russian","Arabic","Dutch"};

        TextView txtLanguage = findViewById(R.id.customize_languages);

        //db.userDao().updateLanguages(email, txtLanguage.getText().toString());

        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtLanguage.setText(languages[i]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showCustomDialog() {
        final Dialog dialog = new Dialog(SignupCustomizeActivity.this);
        dialog.setCancelable(true); // the user will be able to cancel the dialog by clicking outside of it
        dialog.setContentView(R.layout.activity_trial_color);

    }

    */
    public void openPairUpActivity (@Nullable String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);
        startActivity(intent);
    }

    private Boolean validBiography(UserEntity userEntity) {
        if (userEntity.getBiography().length() >= 150) {
            biography.setError("Biography too long. Maximum 150 characters.");
            return false;
        } else {
            biography.setError(null);
            return true;
        }
    }
}

