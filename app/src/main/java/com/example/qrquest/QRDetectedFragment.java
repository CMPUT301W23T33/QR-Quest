package com.example.qrquest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
        bundle.putString("qrName", qrName);

        // display the hashed score
        int qrScore = utilities.hashScore(rawValue);
        binding.qrScoreDisplay.setText(String.valueOf(qrScore));
        bundle.putInt("qrScore", qrScore);

        // display the hashed name
        String qrVisual = utilities.visualRepresentation(rawValue);
        binding.qrNameDisplay.setText(qrName);
        bundle.putString("qrVisual", qrVisual);

        // next button
        binding.buttonNext.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_QRDetectedFragment_to_promptPictureFragment, bundle));

        return view;
    }
}
