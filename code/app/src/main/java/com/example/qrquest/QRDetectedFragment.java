package com.example.qrquest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentQrDetectedBinding;

public class QRDetectedFragment extends Fragment {

    FragmentQrDetectedBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQrDetectedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}
