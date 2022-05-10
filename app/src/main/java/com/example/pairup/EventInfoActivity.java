package com.example.pairup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.EventWithUsers;
import com.example.pairup.db.Reservation;
import com.example.pairup.db.UserEntity;
import com.example.pairup.db.UserWithEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AppDatabase db;
    private UserEntity user;
    private EventEntity event;
    private boolean already_joined;
    private NotificationManager notificationManager;
    private static final String ID_NOTIFICACION = "1";
    private static final String ID_CHANNEL = "PairUp";
    private int notificationId;
    private GoogleMap myMap;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo);

        db = AppDatabase.getInstance(getApplicationContext());
        // Retrieve current user information from shared preferences
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_info);
        mapFragment.getMapAsync(this);

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

            if (prefs.getBoolean("NOTIS", true)){
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, String.valueOf(ID_NOTIFICACION));
                builder.setContentTitle("JOINED NEW EVENT");
                builder.setContentText("Remember to assist on " + event.day + " at " + event.time);
                builder.setSmallIcon(R.drawable.ic_baseline_avatar_24);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(ID_NOTIFICACION, ID_CHANNEL,
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                    builder.setChannelId(ID_NOTIFICACION);
                }
                notificationId = 0;
                notificationManager.notify(notificationId, builder.build());
            }


            finish();
        }
    }

    private void cancel() {
        finish();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        myMap = map;
        myMap.setMyLocationEnabled(true);

        float zoomLevel = map.getMaxZoomLevel() - 5;


        LatLng EventLocation = null;
        if (Geocoder.isPresent()) {
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            try {
                String myAddress = event.getLocation();
                List<Address> addresses = gc.getFromLocationName(myAddress, 10);
                for (Address address : addresses) {
                    EventLocation = new LatLng(address.getLatitude(), address.getLongitude());
                }
            } catch (Exception e) {
                Log.e(this.getLocalClassName(), "Exception getting location", e);
            }
        } else {
            Log.w(this.getLocalClassName(), "Geocoder not available");
        }


        myMap.addMarker(new MarkerOptions().position(EventLocation));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EventLocation, zoomLevel));
    }
}
