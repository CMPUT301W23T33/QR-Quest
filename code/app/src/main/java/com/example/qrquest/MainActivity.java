package com.example.qrquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseFirestore db;
    DocumentReference docRef;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // get user name from Shared Preferences
        SharedPreferences sharedPref = getSharedPreferences("sp", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");

        // initialize database
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("Player").document(username);

        // retrieve region from player data
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot docSnap = task.getResult();
                if (docSnap.exists()) {
                    String region = Objects.requireNonNull(docSnap.get("region")).toString();
                    getMapFromLocation(region);
                }
            } else {
                Log.d("Get document process", "Failed");
            }
        });
    }

    public void getMapFromLocation(String region) {
        // locate the map at the player's location
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;

        try {
            // get latLng from String
            addresses = coder.getFromLocationName(region, 5);

            //check for null
            if (addresses == null)
                return;
            Address location = addresses.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("My location"));

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }
}