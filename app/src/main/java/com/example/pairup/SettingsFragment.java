package com.example.pairup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import android.widget.TextView;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsFragment extends PreferenceFragmentCompat {
    private AppDatabase db;
    TextView isLanguageSelected;
    ArrayList selectedLanguages;
    EditTextPreference username, biography;
    Preference languages, avatar_color;
    int initial_color;
    Preference logout;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
        /* Inflate the layout for this fragment
        //return inflater.inflate(R.xml., container, false);*/

        // Retrieve data passed as intent to PairUpActivity

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        int id = prefs.getInt("ID", 0);
        //UserEntity user = db.userDao().getCurrentUserById((long)id);

        /*
        username = findPreference("edit_username");
        username.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                usernamePicker();
                return true;
            }
        });


        biography = findPreference("edit_biography");
        biography.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                biographyPicker();
                return true;
            }
        });

*/

        languages = findPreference("edit_languages");
        languages.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                languagePicker();
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

    private void usernamePicker() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();
        UserEntity user = db.userDao().getCurrentUser(email);

        dialog.setTitle(getString(R.string.edit_username));
        dialog.setMessage("Your current username is:" + user.getName() + "\n Insert your new username");
        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.userDao().updateUsername(email, username.getText().toString());
            }
        });
        dialog.show();
    }

    private void biographyPicker() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();
        UserEntity user = db.userDao().getCurrentUser(email);

        dialog.setTitle(getString(R.string.edit_bio));
        dialog.setMessage("Your current biography is:" + user.getBiography() + "\n Write your new biography");
        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //db.userDao().updateBiography(email, biography.getText().toString());
            }
        });
        dialog.show();
    }

    public void languagePicker() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();
        UserEntity user = db.userDao().getCurrentUser(email);

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
                PairUpActivity activity = (PairUpActivity) getActivity();
                String email = activity.getCurrentUser();
                //db.userDao().updateLanguages(email, selectedLanguages.toString().substring(1, selectedLanguages.toString().length() - 1));
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
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), initial_color , new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                //avatar.setColorFilter(Color.parseColor(user.getColor()));
                initial_color = color;
                //db.userDao().updateColor(email, "#" + Integer.toHexString(initial_color));
                //sp.edit().putString("color",String.valueOf(color)).commit();
                //avatar.setColorFilter(Color.parseColor(user.getColor()));
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
        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
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


    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void onResume() {
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Settings");

    }
}


