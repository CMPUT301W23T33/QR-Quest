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
import android.graphics.Color;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.List;

/**
 * This class represents the main screen of the app. It prompts for users' location permission and
 * displays a map if granted.
 * @author Thea Nguyen
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    FirebaseFirestore db;
    private GoogleMap map;
    private LocationManager manager;
    private String username;
    private Location currentLocation;

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

        // Camera button
        FloatingActionButton button_camera = findViewById(R.id.button_camera_1);
        button_camera.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("MissingPermission")
    ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
        Boolean fineLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,false);


        if ((fineLocationGranted != null && fineLocationGranted)
        || (coarseLocationGranted != null && coarseLocationGranted)) {
            checkLocationEnabled();

            // setup location manager
            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } else {
            // erase local memories
            SharedPreferences sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();

            // erase the account in firestore
            db.collection("Player").document(username)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("DELETE", "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("DELETE", "Error deleting document", e));

            // terminate the app
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    });

    /**
     * This method turns a string of hex color into a hsv color, which is applicable for changing the
     * markers' color on the map.
     * @param color a string of hexadecimal color (#000000)
     * @return a HSV color for map marker
     */
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    /**
     * This method checks if the location settings of the user is enabled or not.
     */
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

        // find the region
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this);
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(),1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // set region of the user
        db.collection("Player").document(username)
                .update("region", addresses.get(0).getAdminArea())
                .addOnSuccessListener(unused -> Log.d("UPDATE", "Successfully updated"))
                .addOnFailureListener(e -> Log.d("UPDATE", "Error updating document"));

        // set markers for nearby QR codes
        db.collection("QR Code").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    double latitude = Double.parseDouble(String.valueOf(doc.get("latitude")));
                    double longitude = Double.parseDouble(String.valueOf(doc.get("longitude")));
                    float[] list = new float[1];
                    Location.distanceBetween(location.getLatitude(),
                            location.getLongitude(),
                            latitude, longitude, list);

                    // distance radius within 20km (nearby)
                    if (list[0] < 20000) {
                        LatLng latLng1 = new LatLng(latitude, longitude);
                        map.addMarker(new MarkerOptions()
                                .position(latLng1)
                                .title(String.valueOf(doc.get("name")))
                                .icon(getMarkerIcon("#CDB4DB")));
                    }
                }
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null)
            map.clear();

        if (currentLocation != null) {
            db.collection("QR Code").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        double latitude = Double.parseDouble(String.valueOf(doc.get("latitude")));
                        double longitude = Double.parseDouble(String.valueOf(doc.get("longitude")));
                        float[] list = new float[1];
                        Location.distanceBetween(currentLocation.getLatitude(),
                                currentLocation.getLongitude(),
                                latitude, longitude, list);

                        // distance radius within 20km (nearby)
                        if (list[0] < 20000) {
                            LatLng latLng1 = new LatLng(latitude, longitude);
                            map.addMarker(new MarkerOptions()
                                    .position(latLng1)
                                    .title(String.valueOf(doc.get("name")))
                                    .icon(getMarkerIcon("#CDB4DB")));
                        }
                    }
                }
            });
        }
    }
}