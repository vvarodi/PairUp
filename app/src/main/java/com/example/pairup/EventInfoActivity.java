package com.example.pairup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo);


        SharedPreferences prefs = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String email = prefs.getString("EMAIL", "");

        TextView test = findViewById(R.id.test);
        test.setText(email);


    }

}
