package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventDao;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.Reservation;
import com.example.pairup.db.UserEntity;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * HostActivity for creating new Events
 */
public class HostActivity extends AppCompatActivity {

    private AppDatabase db;
    private EventEntity new_event;
    UserEntity user;
    String address = null;
    String date = null;
    String time = null;
    String language = null;
    Integer member = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        setTitle("Host a new meeting");

        db = AppDatabase.getInstance(getApplicationContext());
        new_event = new EventEntity();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int id = prefs.getInt("ID", 0);
        user = db.userDao().getCurrentUserById((long)id);

        int color = Color.parseColor("#AE6118");
        findViewById(R.id.profile1).setBackgroundColor(color);


        Button saveButton = (Button)findViewById(R.id.button_join);
        saveButton.setText("Save");
        saveButton.setOnClickListener(view -> saveEvent());

        findViewById(R.id.button_location).setOnClickListener(view -> openMapsActivity());

        MaterialButtonToggleGroup members = findViewById(R.id.toggle_members);
        members.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.two) {
                        member = 2;
                    }
                    if (checkedId == R.id.four) {
                        member = 4;
                    }
                    new_event.setMembers(member);
                }
            }
        });
    }

    private void saveEvent() {


        if (address == null || date == null || time == null || language == null){

            Toast.makeText(this, "Empty field", Toast.LENGTH_LONG).show();
        } else {
            new_event.setFull(false);
            long inserted_event = db.eventDao().NewEvent(new_event);
            EventEntity inserted = db.eventDao().getEvent(inserted_event);


            Reservation newRes = new Reservation(user.getId_user(), inserted.getId_event());
            //newRes.setId_userRel(us1);
            //newRes.setId_eventRel(ev1);
            Log.d("Assert", "try" + newRes.id_event);
            db.reservationDao().insertReservation(newRes);

            finish();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences sharedPf = getSharedPreferences("IDMaps", 0);
        address = sharedPf.getString("Address",null);
        if (address != null) {
            new_event.setLocation(address);
            TextView txtTime = findViewById(R.id.Location);
            txtTime.setText(address);
        }
    }

    private void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    public void datePicker(View view) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String day, month;
                        TextView txtDate = findViewById(R.id.Date);

                        if (dayOfMonth < 10){
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }

                        if ((monthOfYear + 1) < 10){
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                        }

                        date = day + "/" + month + "/" + year;
                        txtDate.setText(date);

                        // Save as String, reformat when needed
                        new_event.setDay(date);
                    }
                }, mYear, mMonth, mDay);
        // Not showing past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void timePicker(View view) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        TextView txtTime = findViewById(R.id.Time);
                        time = hourOfDay + ":" + minute;
                        txtTime.setText(time);

                        new_event.setTime(time);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void languagePicker(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] languages = {"Italian", "French", "English", "Spanish", "Portuguese", "Japanese","Mandarin", "Russian", "Arabic", "Dutch"};

        TextView txtLanguage = findViewById(R.id.Language);


        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                language = languages[i];
                txtLanguage.setText(language);
                new_event.setLanguage(language);
            }
        });
        // Create and show the Alert Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}