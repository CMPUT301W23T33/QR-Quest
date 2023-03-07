package com.example.qrquest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap map;
    LocationManager manager;

    FirebaseFirestore db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        // setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        // initialize db
        db = FirebaseFirestore.getInstance();

        // get username from Shared Preferences
        SharedPreferences sharedPref = getSharedPreferences("sp", Context.MODE_PRIVATE);
        username = sharedPref.getString("username", "");

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("STYLE", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("STYLE", "Can't find style. Error: ", e);
        }
    }


    @SuppressLint("MissingPermission")
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        if (fineLocationGranted != null && fineLocationGranted
                        || coarseLocationGranted != null && coarseLocationGranted) {
                            checkLocationEnabled();

                            // setup map
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            assert mapFragment != null;
                            mapFragment.getMapAsync(this);
                            if (map != null) {
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
                            }

                            // setup location manager
                            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                        } else {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }
            );

    private void checkLocationEnabled() {
        LocationManager manager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = false;
        boolean networkEnabled = false;

        try {
            GPSEnabled = manager1.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = manager1.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception error) {
            error.printStackTrace();
        }

        if (!GPSEnabled || !networkEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable GPS/Wifi Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", (dialog, which) ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Decline", null)
                    .show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        // find the region
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this);
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(),1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // set region for users
        db.collection("Player").document(username)
                .update("region", addresses.get(0).getAdminArea())
                .addOnSuccessListener(unused -> Log.d("Update region", "Successfully updated"))
                .addOnFailureListener(e -> Log.d("Update region", "Error updating document"));

        manager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}