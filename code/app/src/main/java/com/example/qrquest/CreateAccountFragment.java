package com.example.qrquest;

import static androidx.navigation.Navigation.findNavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrquest.databinding.CreateAccountFragmentBinding;
import com.example.qrquest.databinding.FragmentStartBinding;

public class CreateAccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view binding
        CreateAccountFragmentBinding binding = CreateAccountFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // next button
        binding.buttonBack.setOnClickListener(v ->
                findNavController(view).navigate(R.id.action_createAccountFragment_to_startFragment));
        return view;
    }
}