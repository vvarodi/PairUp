package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;

import java.util.Calendar;
import java.util.Date;

/**
 * HostActivity for creating new Events
 */
public class HostActivity extends AppCompatActivity {

    private AppDatabase db;
    private EventEntity new_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        setTitle("Host a new meeting");

        db = AppDatabase.getInstance(getApplicationContext());
        new_event = new EventEntity();

        Button saveButton = (Button)findViewById(R.id.button_join);
        saveButton.setText("Save");
        //saveButton.setOnClickListener(view -> saveEvent());

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
                        TextView txtDate = findViewById(R.id.Date);
                        txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        // Save as String, reformat when needed
                        new_event.setDay(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
                        txtTime.setText(hourOfDay + ":" + minute);

                        //new_event.setTime(hourOfDay + ":" + minute);
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
                txtLanguage.setText(languages[i]);
            }
        });
        // Create and show the Alert Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}