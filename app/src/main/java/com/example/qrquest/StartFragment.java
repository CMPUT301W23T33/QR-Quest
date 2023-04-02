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

/**
 * This fragment represents the Start Screen of the app.
 * @author Dang Viet Anh Dinh
 */

public class StartFragment extends Fragment {

    /**
     * Called to have the fragment instantiate its user interface view; inflates the layout, initializes the UI elements.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view binding
        FragmentStartBinding binding = FragmentStartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // next button
        binding.buttonNext.setOnClickListener(v ->
                findNavController(view).navigate(R.id.action_startFragment_to_createAccountFragment));
        return view;
    }
}
