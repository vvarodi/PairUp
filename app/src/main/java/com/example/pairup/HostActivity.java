package com.example.pairup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
                        //TextView txtDate = findViewById(R.id.date_show);
                        //txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        // Save as String, reformat when needed
                        new_event.setDay(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        new_event.setTitle(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        db.eventDao().NewEvent(new_event);
                        setResult(RESULT_OK);
                        finish();

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
                        //TextView txtTime = findViewById(R.id.time_show);
                        //txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


}