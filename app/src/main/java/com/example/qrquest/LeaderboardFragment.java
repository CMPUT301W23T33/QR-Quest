package com.example.qrquest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment {

    FragmentLeaderboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonBack.setOnClickListener(v -> requireActivity().finish());

        return view;
    }
}
