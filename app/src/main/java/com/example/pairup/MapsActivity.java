package com.example.pairup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private TextView text_clicked;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        text_clicked = (TextView)findViewById(R.id.text_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setOnPoiClickListener(this);

        float zoomLevel = map.getMaxZoomLevel() - 5;
        LatLng uc3m = new LatLng(40.332690215919904, -3.765110001822394); // UC3M Leganés

        map.addMarker(new MarkerOptions().position(uc3m));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(uc3m, zoomLevel));
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {

        //update();
        Toast.makeText(this, "Clicked: " + poi.name + "\nPlace ID:" + poi.placeId + "\nLatitude:" +
                poi.latLng.latitude + " Longitude:" + poi.latLng.longitude, Toast.LENGTH_SHORT)
                .show();

        text_clicked.setText(poi.name);
    }

}