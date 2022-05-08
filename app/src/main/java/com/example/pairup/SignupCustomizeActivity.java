package com.example.pairup;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Activity to ask new users some information about their profile:
 *      - Avatar Color
 *      - Short Biography (max 150 character length)
 *      - Languages they speak or want to learn
 *  User can fill this information now and SAVE or SKIP and edit later
 */
public class SignupCustomizeActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserEntity user;

    EditText biography;
    TextView isLanguageSelected;
    ArrayList selectedLanguages;
    ImageView avatar;
    int initial_color;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupcustomize);

        // To update user information. Get current user entity
        db = AppDatabase.getInstance(getApplicationContext());
        String email = getIntent().getExtras().getString("GMAIL");
        user = db.userDao().getCurrentUser(email);

        // Layout information
        biography = findViewById(R.id.customize_biography);
        avatar = findViewById(R.id.customize_avatar);
        // initial_color is the initially-selected color to be shown in the rectangle on the left of the arrow in COLOR DIALOG
        initial_color = Color.parseColor(user.getColor());
        // Initialize
        selectedLanguages = new ArrayList();

        // Click Listeners of all buttons
        findViewById(R.id.customize_color).setOnClickListener(view -> selectAvatarColor());
        findViewById(R.id.customize_languages).setOnClickListener(view -> languagePicker());
        findViewById(R.id.customize_save).setOnClickListener(view -> RegisterCustomization());
        findViewById(R.id.customize_skip).setOnClickListener(view -> Skip());
    }

    /**
     * Creates a color Picker Dialog where user can pick a color for the User Avatar
     *
     * Android Color Picker Library from:
     * https://github.com/yukuku/ambilwarna
     */
    private void selectAvatarColor() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(SignupCustomizeActivity.this, initial_color , new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // Update initial_color variable so next time user opens dialog the initial color is the last picked
                initial_color = color;
                // Change avatar color in layout
                avatar.setColorFilter(color);
                // Update User Color in DB
                db.userDao().updateColor(user.getId_user(), "#" + Integer.toHexString(initial_color));
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // the dialog is closed
            }
        });
        dialog.show();
    }


    /**
     * Select Language Dialog with Multi Choice languages
     * Clicked items last time opened dialog will appear checked next time you open it
     * Only if SAVE is clicked, DB user languages are updated
     */
    public void languagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Languages interested in:");

        // MultiChoice OPTIONS
        String[] languages_list = {"Italian", "French", "English", "Spanish", "Portuguese", "Japanese", "Mandarin", "Russian", "Arabic", "Dutch"};
        boolean[] checkedItems = {false, false, false, false, false, false, false, false, false, false};

        // update checked items
        for (int j = 0; j < checkedItems.length; j++){
            if (selectedLanguages.contains(languages_list[j])){
                checkedItems[j] = true;
            }
        }

        builder.setMultiChoiceItems(languages_list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    selectedLanguages.add(languages_list[i]);
                } else {
                    selectedLanguages.remove(languages_list[i]);
                }
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                String string_languages = TextUtils.join(", ", selectedLanguages);
                db.userDao().updateLanguages(user.getId_user(), string_languages);
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

    /**
     * Register User Customizations (if any) when SAVE button is clicked
     * Update DB
     * Go to PairUpActivity
     */
    public void RegisterCustomization() {
        
        openPairUpActivity(user.getGmail());
    }


    /*
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
    }*/

    /**
     * Skip Button to users who do not want to customize their Profile
     * (They can do it later: Settings -> Edit Profile)
     * Opens PairUpActivity
     */
    private void Skip() {
        openPairUpActivity(user.getGmail());
    }

    /**
     * Open PairUpActivity
     * @param gmail: current user gmail to query
     */
    public void openPairUpActivity (String gmail){
        Intent intent = new Intent(this, PairUpActivity.class);
        intent.putExtra("GMAIL", gmail);
        startActivity(intent);
        finish();
    }

}

