package com.example.qrquest;

import static androidx.navigation.Navigation.findNavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.app.AlertDialog;
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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrquest.databinding.CreateAccountFragmentBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This fragment represents the Create Account Screen.
 * Creates an account with a unique username and defined location (region).
 * A local file will be created to store the username.
 * @author Thea Nguyen
 */
public class CreateAccountFragment extends Fragment implements LocationListener {
    FirebaseFirestore db;
    LocationManager manager;
    Player newPlayer;
    CollectionReference playerRef;
    String randomName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view binding
        CreateAccountFragmentBinding binding = CreateAccountFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // next button
        binding.buttonBack.setOnClickListener(v ->
                findNavController(view).navigate(R.id.action_createAccountFragment_to_startFragment));


        // database
        db = FirebaseFirestore.getInstance();
        playerRef = db.collection("Player");
        newPlayer = new Player();

        // get region
        grantPermission();
        checkLocationEnabled();
        getLocation();

        // generate unique name
        randomName = UUID.randomUUID().toString().substring(0, 8);
        binding.nameText.setText(randomName);

        // nice button (primary button)
        binding.buttonElevatedPrimary.setOnClickListener(v -> {
            newPlayer.setName(randomName);
            playerRef.document(randomName)
                    .set(newPlayer)
                    .addOnSuccessListener(unused -> Log.d("TEST", "Added document successfully"))
                    .addOnFailureListener(e -> Log.d("TEST", "Error adding document"));
        });

        // another button (secondary button) (re-generate)
        binding.buttonElevatedSecondary.setOnClickListener(v -> {
            randomName = UUID.randomUUID().toString().substring(0, 8);
            binding.nameText.setText(randomName);
        });

        return view;
    }

    /**
     * This method obtains location of the device using Network Provider.
     * If permissions are granted, the location updates are requested with a min time interval of
     * 500 milliseconds and a minimum distance interval of 5 meters.
     * @throws SecurityException if an app does not have all necessary permissions to access
     * location information
     */
    private void getLocation() throws SecurityException{
        try {
            manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, this);
        } catch (SecurityException error) {
            error.printStackTrace();
        }

    }

    /**
     * This method checks if location service is enabled or not.
     * If location services are not enabled, an alert dialog is displayed, prompting user to enable them.
     */
    private void checkLocationEnabled() {
        LocationManager manager1 = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
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
            new AlertDialog.Builder(requireContext())
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", (dialog, which) ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Decline", null)
                    .show();
        }
    }

    /**
     * This method requests permission to access user's location.
     * If the permission has not been granted, a dialog box will be displayed prompting for the permission.
     * If the permission is already granted, the method does nothing.
     * @throws SecurityException if user denies giving location access permission
     */
    private void grantPermission() throws SecurityException{
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            newPlayer.setRegion(addresses.get(0).getAdminArea());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the username locally
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        if (!sharedPref.contains("username")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", randomName);
            editor.apply();
        }
    }
}