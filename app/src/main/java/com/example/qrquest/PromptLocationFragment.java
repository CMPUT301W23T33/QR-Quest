package com.example.qrquest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qrquest.databinding.FragmentPromptBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;
import java.util.Locale;

/**
 * This class represents the Prompt Location Screen
 * and gets the geo-location of the scanned QR code.
 * @author Thea Nguyen
 */
public class PromptLocationFragment extends Fragment {
    FragmentPromptBinding binding;
    private Bundle bundle;
    private View view;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPromptBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // change title
        binding.addPictureTitle.setText(R.string.add_a_location);
        binding.addPictureBonus.setText(R.string.add_location_bonus);

        // set up fused location client of google services api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    enablingButtons(location);
                    Log.d("PromptLocation", location.toString());
                }
            }
        };

        // get bundle (contain picture URI + raw value)
        bundle = getArguments();
        assert bundle != null;

        binding.buttonSorry.setEnabled(false);
        binding.buttonSure.setEnabled(false);
        binding.buttonSorry.setAlpha(.5f);
        binding.buttonSure.setAlpha(.5f);
        startLocationUpdates();
        return view;
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).build();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        createLocationRequest();
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void enablingButtons(@NonNull Location location) {
        // get latitude, longitude
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // enable button
        binding.buttonSorry.setEnabled(true);
        binding.buttonSure.setEnabled(true);

        binding.buttonSorry.setAlpha(1.0f);
        binding.buttonSure.setAlpha(1.0f);

        // transfer (x, y)
        binding.buttonSure.setOnClickListener(v -> {
            bundle.putString("latitude", String.valueOf(latitude));
            bundle.putString("longitude", String.valueOf(longitude));
            Navigation.findNavController(view).navigate(
                    R.id.action_promptLocationFragment_to_editQRFragment, bundle);
        });
        binding.buttonSorry.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_promptLocationFragment_to_editQRFragment, bundle));
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
}
