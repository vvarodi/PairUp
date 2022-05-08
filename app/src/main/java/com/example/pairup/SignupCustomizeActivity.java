package com.example.pairup;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SignupCustomizeActivity extends AppCompatActivity {

    private AppDatabase db;

    EditText biography;
    Button save, skip, select_color;
    TextView isLanguageSelected;
    ArrayList selectedLanguages;
    ImageView avatar;
    int initial_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupcustomize);

        db = AppDatabase.getInstance(getApplicationContext());
        String email = getIntent().getExtras().getString("GMAIL");
        UserEntity user = db.userDao().getCurrentUser(email);

        // AVATAR COLOR
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        if (sp.getString("color","").equals("")) {
        }else{
            initial_color = (int) Double.parseDouble(sp.getString("color",""));
        }

        avatar = findViewById(R.id.image);

        select_color = findViewById(R.id.choose_color);
        initial_color = Color.parseColor(user.getColor());

        select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(SignupCustomizeActivity.this, initial_color , new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        avatar.setColorFilter(Color.parseColor(user.getColor()));
                        initial_color = color;
                        db.userDao().updateColor(email, "#" + Integer.toHexString(initial_color));
                        sp.edit().putString("color",String.valueOf(color)).commit();
                    }
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }
                });
                dialog.show();
            }
        });
        avatar.setColorFilter(Color.parseColor(user.getColor()));

        // BIOGRAPHY, LANGUAGES AND SAVE, SKIP BUTTONS
        biography = findViewById(R.id.customize_biography);

        save = findViewById(R.id.customize_save);
        save.setOnClickListener(view -> RegisterCustomization());

        skip = findViewById(R.id.customize_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserEntity user = db.userDao().getCurrentUser(email);
                openPairUpActivity(user.getGmail());
            }
        });
    }

    // When user clicks Save Button, Registers their profile customization
    public void RegisterCustomization() {
        String email = getIntent().getExtras().getString("GMAIL");
        UserEntity user = db.userDao().getCurrentUser(email);
        if (validInput()) {
            db.userDao().updateBiography(email, biography.getText().toString());
            db.userDao().updateLanguages(email, selectedLanguages.toString().substring(1, selectedLanguages.toString().length()-1));
            openPairUpActivity(user.getGmail());
            Toast.makeText(SignupCustomizeActivity.this, "PROFILE CUSTOMIZATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
        }
    }

    // LANGUAGE DIALOG, multichoice
    public void languagePicker(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        selectedLanguages = new ArrayList();

        String email = getIntent().getExtras().getString("GMAIL");
        String[] languages_list = {"Italian", "French", "English", "Spanish", "Portuguese", "Japanese", "Mandarin", "Russian", "Arabic", "Dutch"};

        builder.setMultiChoiceItems(languages_list, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    if (isLanguageSelected == null) {
                        isLanguageSelected = biography;
                        //selectedLanguagesString = languages_list[i].toString() + " ";
                        selectedLanguages.add(languages_list[i]);
                    } else {
                        //selectedLanguagesString += languages_list[i].toString() + " ";
                        selectedLanguages.add(languages_list[i]);
                    }
                }
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                //db.userDao().updateLanguages(email, selectedLanguages.toString().substring(1, selectedLanguages.toString().length()-1));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();

            }
        });
        // Create and show the Alert Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openPairUpActivity (@Nullable String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);
        startActivity(intent);
    }

    public Boolean validInput() {
        boolean allFine = true;
        if (!validBiography()) {
            allFine = false;
        }
        if (!validLanguages()) {
            allFine = false;
        }
        return allFine;
    }

    private Boolean validBiography() {
        String bio = biography.getText().toString();
        if (bio.length() == 0) {
            biography.setError("Biography cannot be empty");
            return false;
        }else if (bio.length() >= 150) {
            biography.setError("Biography too long. Maximum 150 characters.");
            return false;
        } else {
            biography.setError(null);
            return true;
        }
    }

    private Boolean validLanguages() {
        if (selectedLanguages.toString().length() == 0) {
            isLanguageSelected.setError("You have to select at least 1 language");
            return false;
        } else {
            isLanguageSelected.setError(null);
            return true;
        }
    }

}

