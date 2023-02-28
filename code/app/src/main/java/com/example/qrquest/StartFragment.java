package com.example.qrquest;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view binding
        com.example.qrquest.databinding.FragmentStartBinding binding = FragmentStartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // next button
        binding.buttonNext.setOnClickListener(v ->
                findNavController(view).navigate(R.id.action_startFragment_to_createAccountFragment));
        return view;
    }
}
