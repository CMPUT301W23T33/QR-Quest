package com.example.qrquest;

import static androidx.navigation.Navigation.findNavController;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Player newPlayer;
    private CollectionReference playerRef;
    private String randomName;
    private int createAccount = 0;
    private CreateAccountFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view binding
        binding = CreateAccountFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // next button
        binding.buttonBack.setOnClickListener(v ->
                findNavController(view).navigate(R.id.action_createAccountFragment_to_startFragment));

        permission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        permission.launch(Manifest.permission.ACCESS_COARSE_LOCATION);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the username locally
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        if (!sharedPref.contains("username") && createAccount == 1) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", randomName);
            editor.apply();
        }
    }

    /**
     * This creates an Activity Result Launcher to synchronously wait for user permission.
     */
    private final ActivityResultLauncher<String> permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            checkLocationEnabled();
            getLocation();

            // database
            db = FirebaseFirestore.getInstance();
            playerRef = db.collection("Player");
            newPlayer = new Player();

            // generate unique name
            randomName = UUID.randomUUID().toString().substring(0, 15);
            binding.nameText.setText(randomName);

            // nice button (primary button)
            binding.buttonElevatedPrimary.setOnClickListener(v -> {
                createAccount = 1;
                newPlayer.setUsername(randomName);
                playerRef.document(newPlayer.getUsername())
                        .set(newPlayer)
                        .addOnSuccessListener(unused -> Log.d("TEST", "Added document successfully"))
                        .addOnFailureListener(e -> Log.d("TEST", "Error adding document"));

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            });

            // another button (secondary button) (re-generate)
            binding.buttonElevatedSecondary.setOnClickListener(v -> {
                randomName = UUID.randomUUID().toString().substring(0, 15);
                binding.nameText.setText(randomName);
            });
        }
    });

    /**
     * This method requests the device's location using the LocationManager and the NETWORK_PROVIDER.
     * The method registers for location updates with a minimum time interval of 0 milliseconds and a
     * minimum distance of 0 meters.
     */
    private void getLocation() {
        try {
            manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } catch (SecurityException error) {
            error.printStackTrace();
        }

    }

    /**
     * This method checks whether the device's location services are enabled.
     * If all the necessary services are enable, it does nothing.
     * Else, an alert dialog will be displayed prompting permissions from the user.
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
}