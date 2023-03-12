package com.example.qrquest;


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

public class PromptPictureFragment extends Fragment {

    FragmentPromptBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPromptBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // get the raw value of the QR code
        Bundle bundle = getArguments();
        if (bundle != null) {
            String raw = bundle.getString("rawValue");
            Toast.makeText(requireActivity(), raw, Toast.LENGTH_SHORT).show();
        }

        binding.buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_promptPictureFragment_to_camera, bundle);
            }
        });

        binding.buttonSorry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_promptPictureFragment_to_promptLocationFragment);
            }
        });


        return view;
    }
}