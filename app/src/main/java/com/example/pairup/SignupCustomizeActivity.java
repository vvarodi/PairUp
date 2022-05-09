package com.example.pairup;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
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

    private int initial_color;
    private EditText biography;
    private ArrayList<String> selectedLanguages;
    private ImageView avatar;
    private String string_languages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupcustomize);

        // To update user information. Get current user entity
        db = AppDatabase.getInstance(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int id = prefs.getInt("ID", 0);
        user = db.userDao().getCurrentUserById(id);

        // Initializations
        biography = findViewById(R.id.customize_biography);
        avatar = findViewById(R.id.customize_avatar);
        // initial_color is the initially-selected color to be shown in the rectangle on the left of the arrow in COLOR DIALOG
        initial_color = Color.parseColor(user.getColor());
        string_languages = ""; // Not Null if not selected items when update db
        selectedLanguages = new ArrayList<>();

        // Click Listeners of all buttons
        // AVATAR   LANGUAGES   SKIP  SAVE
        findViewById(R.id.customize_color).setOnClickListener(view -> avatarColorPicker());
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
    private void avatarColorPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(SignupCustomizeActivity.this, initial_color , new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // Update initial_color variable so next time user opens dialog the initial color is the last picked
                initial_color = color;
                // Change avatar color in layout
                avatar.setColorFilter(color);
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
     * Clicked items last time opened dialog will appear checked next time you open it again
     * Only if SAVE is clicked, DB user languages are updated
     */
    private void languagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Languages interested in:");

        // MultiChoice OPTIONS
        String[] languages_list = {"Italian", "French", "English", "Spanish", "Portuguese", "Japanese", "Mandarin", "Russian", "Arabic", "Dutch"};
        boolean[] checkedItems = {false, false, false, false, false, false, false, false, false, false};

        // update checked items last time Dialog opened
        for (int j = 0; j < checkedItems.length; j++){
            if (selectedLanguages.contains(languages_list[j])){
                checkedItems[j] = true;
            }
        }

        builder.setMultiChoiceItems(languages_list, checkedItems, (dialogInterface, i, isChecked) -> {
            if (isChecked) {
                selectedLanguages.add(languages_list[i]);
            } else {
                selectedLanguages.remove(languages_list[i]);
            }
        });

        builder.setPositiveButton(R.string.save, (dialogInterface, id) -> string_languages = TextUtils.join(", ", selectedLanguages));
        builder.setNegativeButton(R.string.cancel, (dialogInterface, id) -> dialogInterface.dismiss());
        // Create and show the Alert Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Register User Customizations (if any) when SAVE button is clicked
     * Update DB
     * Go to PairUpActivity
     */
    private void RegisterCustomization() {

        // Update User Color in DB
        db.userDao().updateColor(user.getId_user(), "#" + Integer.toHexString(initial_color));
        // Update Biography
        db.userDao().updateBiography(user.getId_user(), biography.getText().toString());
        // Update Languages
        db.userDao().updateLanguages(user.getId_user(), string_languages);

        openPairUpActivity();
    }



    /**
     * Skip Button to users who do not want to customize their Profile
     * (They can do it later: Settings -> Edit Profile)
     * Opens PairUpActivity
     */
    private void Skip() {
        Toast.makeText(this, getString(R.string.skipped), Toast.LENGTH_SHORT).show();
        openPairUpActivity();
    }

    /**
     * Open PairUpActivity
     */
    private void openPairUpActivity (){
        Intent intent = new Intent(this, PairUpActivity.class);
        startActivity(intent);
        finish();
    }

}

