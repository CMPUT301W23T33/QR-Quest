package com.example.qrquest;

import static androidx.navigation.Navigation.findNavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrquest.databinding.CreateAccountFragmentBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * This fragment represents the Create Account Screen.
 * Creates an account with a unique username and defined location (region).
 * A local file will be created to store the username.
 * @author Thea Nguyen
 */
public class CreateAccountFragment extends Fragment {
    FirebaseFirestore db;
    private Player newPlayer;
    private CollectionReference playerRef;
    private String randomName;
    private int createAccount = 0;
    private CreateAccountFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view binding
        binding = CreateAccountFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // database
        db = FirebaseFirestore.getInstance();
        playerRef = db.collection("Player");
        newPlayer = new Player();

        // generate unique name
        randomName = Utilities.hashName(UUID.randomUUID().toString());
        binding.nameText.setText(randomName);

        // back button
        binding.buttonBack.setOnClickListener(v ->
                findNavController(view).navigate(R.id.action_createAccountFragment_to_startFragment));

        // nice button (primary button)
        binding.buttonElevatedPrimary.setOnClickListener(v -> {
            createAccount = 1;
            newPlayer.setUsername(randomName);
            playerRef.document(newPlayer.getUsername())
                    .set(newPlayer)
                    .addOnSuccessListener(unused -> Log.d("SET", "Added document successfully"))
                    .addOnFailureListener(e -> Log.d("SET", "Error adding document"));

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        // another button (secondary button) (re-generate)
        binding.buttonElevatedSecondary.setOnClickListener(v -> {
            randomName = Utilities.hashName(UUID.randomUUID().toString());
            binding.nameText.setText(randomName);
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the username locally
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp",Context.MODE_PRIVATE);

        if (!sharedPref.contains("username") && createAccount == 1) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", randomName);
            editor.apply();
        }
    }
}