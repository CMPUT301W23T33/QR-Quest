package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrquest.databinding.EditProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * This class defines the edit fragment screen
 * @author Dang Viet Anh Dinh
 */
public class EditProfileFragment extends Fragment {

    FirebaseFirestore db;
    EditProfileBinding binding;
    private String emailAddress;
    private String phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get username
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
//        String username = sharedPref.getString("username", "");
        String username = "UI5";

        binding.editProfileName.setText(username);

        // Set user's phone number
        // phoneNumber.setText(phoneNumber);

        // Get and set user's email address
        db.collection("Player").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()){
                    EditProfileFragment.this.emailAddress = document.getString("emailAddress");
                    binding.editProfileEmailAddress.setText(EditProfileFragment.this.emailAddress);
                }
            }
        });

        // Navigate back to the profile screen
        binding.editProfileButtonBack.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_profileFragment));

        return view;

    }
}