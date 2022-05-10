package com.example.pairup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

import java.util.ArrayList;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsFragment extends PreferenceFragmentCompat {
    private AppDatabase db;
    TextView isLanguageSelected;
    ArrayList selectedLanguages;
    EditTextPreference username, biography;
    Preference languages, avatar_color, logout, app_language;
    int initial_color;
    UserEntity user;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        db = AppDatabase.getInstance(requireActivity().getApplicationContext());
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        int id = prefs.getInt("ID", 0);
        user = db.userDao().getCurrentUserById((long)id);


        // NOTIFICATIONS ON - OFF
        SwitchPreferenceCompat notification = (SwitchPreferenceCompat) findPreference("notifications");
        notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                notification.setChecked(((boolean)newValue));

                if (notification.isChecked()){
                    prefs.edit().putBoolean("NOTIS", true).apply();
                    Toast.makeText(getContext(), "Enable notifications", Toast.LENGTH_SHORT).show();
                } else {
                    prefs.edit().putBoolean("NOTIS", false).apply();
                    Toast.makeText(getContext(), "Disable notifications", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        username = findPreference("edit_username");
        username.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

                String new_user = (String) newValue;
                db.userDao().updateUsername(user.getId_user(), new_user);
                return false;
            }
        });


        biography = findPreference("edit_biography");
        biography.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                String new_bio = (String) newValue;
                if (new_bio.length() > 150){
                    Toast.makeText(getContext(), "Too long", Toast.LENGTH_SHORT).show();
                } else {
                    db.userDao().updateBiography(user.getId_user(), new_bio);
                }
                return false;
            }
        });

        languages = findPreference("edit_languages");
        languages.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                languagePicker();
                return true;
            }
        });

        app_language = findPreference("app_language");
        app_language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                appLanguagePicker();
                return true;
            }
        });

        avatar_color = findPreference("edit_color");
        avatar_color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                colorPicker();
                return true;
            }
        });

        logout = findPreference("logout");
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                logoutFunction();
                return true;
            }
        });
    }

    public void languagePicker() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(getString(R.string.edit_languages));
        selectedLanguages = new ArrayList();

        String[] languages_list = {"Italian", "French", "English", "Spanish", "Portuguese", "Japanese", "Mandarin", "Russian", "Arabic", "Dutch"};

        dialog.setMultiChoiceItems(languages_list, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    if (isLanguageSelected == null) {
                        //isLanguageSelected = ;
                        selectedLanguages.add(languages_list[i]);
                    } else {
                        selectedLanguages.add(languages_list[i]);
                    }
                }
            }
        });
        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                String string_languages = TextUtils.join(", ", selectedLanguages);
                db.userDao().updateLanguages(user.getId_user(), string_languages);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    public void colorPicker() {
        initial_color = Color.parseColor(user.getColor());
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), initial_color , new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                initial_color = color;
                db.userDao().updateColor(user.getId_user(), "#" + Integer.toHexString(initial_color));
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        dialog.show();
    }

    private void logoutFunction() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.logout));
        dialog.setMessage("Are you sure you want to log out?");
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences prefs = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
                prefs.edit().putBoolean("LOGGED", false).apply();
                prefs.edit().putInt("ID", 0).apply();
                getActivity().finish();
                }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public void appLanguagePicker() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Choose App Language");

        String[] languages = {"English", "Español", "বাংলা", "中國人", "Italiano", "Deutsch"};

        dialog.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    setLocale("en");
                    getActivity().recreate();
                }
                if (i == 1){
                    setLocale("es");
                    getActivity().recreate();
                }
                if (i == 2){
                    setLocale("bn");
                    getActivity().recreate();
                }
                if (i == 3){
                    setLocale("zh");
                    getActivity().recreate();
                }
                if (i == 4){
                    setLocale("it");
                    getActivity().recreate();
                }
                if (i == 5){
                    setLocale("de");
                    getActivity().recreate();
                }
            }
        });
        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void onResume() {
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Settings");

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,getActivity().getBaseContext().getResources().getDisplayMetrics());

        //save data to shared preferences
        SharedPreferences.Editor editor = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE).edit();
        editor.putString("LANG", lang);
        editor.apply();
    }

    // load languages saved in shared preferences
    public  void loadLocale() {
        SharedPreferences prefs = getContext().getSharedPreferences("Settings", getContext().MODE_PRIVATE);
        String language = prefs.getString("LANG", "");
        setLocale(language);
    }
}


