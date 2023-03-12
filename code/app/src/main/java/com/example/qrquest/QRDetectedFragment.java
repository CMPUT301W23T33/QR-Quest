package com.example.qrquest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentQrDetectedBinding;

/**
 * This class represents the QR Detected Screen. It also does hashing for a name, score, and a visual
 * representation from the raw value scanned from the QR code.
 * @author Zijing Lu
 * @author Jay Pasrija
 * @author Michael Wolowyk
 * @author Thea Nguyen
 */
public class QRDetectedFragment extends Fragment {

    FragmentQrDetectedBinding binding;
    String rawValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQrDetectedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Utilities utilities = new Utilities();

        // get the raw value of the QR code
        Bundle bundle = getArguments();
        assert bundle != null;
        rawValue = bundle.getString("rawValue");

        // display the hashed name
        String qrName = utilities.hashName(rawValue);
        binding.qrNameDisplay.setText(qrName);

        // display the hashed score
        int qrScore = utilities.hashScore(rawValue);
        binding.qrScoreDisplay.setText(String.valueOf(qrScore));

        // next button
        return view;
    }
}
