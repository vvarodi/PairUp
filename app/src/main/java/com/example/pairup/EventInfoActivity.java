package com.example.pairup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.EventWithUsers;
import com.example.pairup.db.Reservation;
import com.example.pairup.db.UserEntity;
import com.example.pairup.db.UserWithEvent;

import java.util.ArrayList;
import java.util.List;

public class EventInfoActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserEntity user;
    private EventEntity event;
    private boolean already_joined;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo);

        db = AppDatabase.getInstance(getApplicationContext());
        // Retrieve current user information from shared preferences
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int id = prefs.getInt("ID", 0);
        user = db.userDao().getCurrentUserById((long)id);
        int id_event = prefs.getInt("ID_EVENT", 0);
        event = db.eventDao().getEvent(id_event);

        already_joined = false;
        List<EventWithUsers> all = db.reservationDao().getEventsWithUsers();
        for (int i=0; i < all.size(); i++){
            if (event.getId_event() == all.get(i).event.getId_event()){
                for (int j = 0; j < all.get(i).users.size(); j++){
                    if (all.get(i).users.get(j).getId_user() == user.getId_user()){
                        already_joined = true;
                    }
                }
            }

        }

        customizeEventData();

        findViewById(R.id.join_save).setOnClickListener(view -> joinEvent());
        findViewById(R.id.cancel).setOnClickListener(view -> cancel());
    }

    private void customizeEventData(){
        TextView loc, date, time;
        loc = findViewById(R.id.InfoLocation);
        date = findViewById(R.id.InfoDate);
        time = findViewById(R.id.InfoTime);
        loc.setText(event.getLocation());
        date.setText(event.getDay());
        time.setText(event.getTime());


    }

    private void joinEvent() {
        if (event.isFull()){
            Toast.makeText(this, "The event is full", Toast.LENGTH_LONG).show();
        } else if (already_joined){
            Toast.makeText(this, "Already Joined", Toast.LENGTH_LONG).show();
        } else {
            Integer joined = event.getJoined();
            joined = joined + 1;
            db.eventDao().updateJoined(joined, event.getId_event());
            if (joined == event.getMembers()){
                db.eventDao().updateFull(true, event.getId_event());
            }
            Reservation newRes = new Reservation(user.getId_user(), event.getId_event());
            db.reservationDao().insertReservation(newRes);
            Toast.makeText(this, "Successfully Joined", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void cancel() {
        finish();
    }

}
