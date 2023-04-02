package com.example.qrquest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qrquest.databinding.FragmentPromptBinding;

/**
 * This class represents the Prompt Picture Screen and navigates the user back to the Camera Screen
 * if the user wants to.
 * @author Thea Nguyen
 */
public class PromptPictureFragment extends Fragment {

    FragmentPromptBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPromptBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // get the raw value of the QR code
        Bundle bundle = getArguments();
        assert(bundle != null);
        bundle.putString("Intent", "Take a picture");

        binding.buttonSure.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_promptPictureFragment_to_camera, bundle));

        binding.buttonSorry.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_promptPictureFragment_to_promptLocationFragment, bundle));


        return view;
    }
}
