package com.example.qrquest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qrquest.databinding.FragmentPromptBinding;

public class PromptLocationFragment extends Fragment {

    FragmentPromptBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPromptBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // change title
        binding.addPictureTitle.setText(R.string.add_a_location);
        binding.addPictureBonus.setText(R.string.add_location_bonus);

        // get bundle (contain picture URI)
        Bundle bundle = getArguments();

        // get latitude, longitude
        binding.buttonSure.setOnClickListener(v -> {
            LocationManager lm = (LocationManager)requireActivity().getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission")
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            // transfer (x, y)
            if (bundle != null) {
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
            }

            // navigate
            Navigation.findNavController(view).navigate(R.id.action_promptLocationFragment_to_editQRFragment);
        });

        binding.buttonSorry.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_promptLocationFragment_to_editQRFragment));



        return view;
    }
}
