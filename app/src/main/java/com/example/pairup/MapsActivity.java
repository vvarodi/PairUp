package com.example.pairup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private TextView text_clicked;
    private GoogleMap myMap;
    private String result = null;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        text_clicked = (TextView)findViewById(R.id.text_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        findViewById(R.id.save).setOnClickListener(view -> saveAndFinish());
        findViewById(R.id.cancel).setOnClickListener(view -> close());
    }

    private void close() {
        finish();
    }

    private void saveAndFinish() {
        if (result != null){
            SharedPreferences mPrefs = getSharedPreferences("IDMaps", 0);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("Address", result);
            editor.apply();

            finish();
        } else {
            Toast.makeText(this, "Click a Location", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
        myMap.setMyLocationEnabled(true);
        myMap.setOnPoiClickListener(this);

        float zoomLevel = map.getMaxZoomLevel() - 5;
        LatLng Madrid = new LatLng(40.4165, -3.70256);
        //myMap.addMarker(new MarkerOptions().position(uc3m));

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Madrid, zoomLevel));
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {

        myMap.clear(); // to not keep last clicked places
        //update();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(poi.latLng.latitude, poi.latLng.longitude, 1);

            Address address = addresses.get(0);
            result = address.getAddressLine(0);
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Exception getting location", e);
        }

        Toast.makeText(this, "Clicked: " + poi.name + "\nPlace ID:" + poi.placeId + "\nLatitude:" +
                poi.latLng.latitude + " Longitude:" + poi.latLng.longitude, Toast.LENGTH_SHORT)
                .show();

        text_clicked.setText(result);
        LatLng clicked = new LatLng(poi.latLng.latitude, poi.latLng.longitude);
        myMap.addMarker(new MarkerOptions().position(clicked));
    }

}