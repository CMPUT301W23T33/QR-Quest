package com.example.qrquest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qrquest.databinding.FragmentPromptBinding;

import java.util.List;

/**
 * This class represents the Prompt Location Screen and gets the geo-location of the scanned QR code.
 * @author Thea Nguyen
 */
public class PromptLocationFragment extends Fragment implements LocationListener {

    FragmentPromptBinding binding;
    private Bundle bundle;
    private View view;
    private LocationManager manager;
    double latitude;
    double longitude;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPromptBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // change title
        binding.addPictureTitle.setText(R.string.add_a_location);
        binding.addPictureBonus.setText(R.string.add_location_bonus);

        // set up location manager
        manager = (LocationManager)requireActivity().getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // get bundle (contain picture URI + raw value)
        bundle = getArguments();
        assert bundle != null;

        binding.buttonSorry.setEnabled(false);
        binding.buttonSure.setEnabled(false);
        binding.buttonSorry.setAlpha(.5f);
        binding.buttonSure.setAlpha(.5f);

        binding.buttonSure.setOnClickListener(v ->
            Navigation.findNavController(view).navigate(R.id.action_promptLocationFragment_to_editQRFragment, bundle));

        binding.buttonSorry.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_promptLocationFragment_to_editQRFragment, bundle));

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        // get latitude, longitude
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // transfer (x, y)
        bundle.putString("latitude", String.valueOf(latitude));
        bundle.putString("longitude", String.valueOf(longitude));

        binding.buttonSorry.setEnabled(true);
        binding.buttonSure.setEnabled(true);

        binding.buttonSorry.setAlpha(1);
        binding.buttonSure.setAlpha(1);

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
}
