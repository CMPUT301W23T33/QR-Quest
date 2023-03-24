package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrquest.databinding.EditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

/**
 * This class defines the edit fragment screen
 * @author Dang Viet Anh Dinh
 */
public class EditProfileFragment extends Fragment {

    private FirebaseFirestore db;
    private EditProfileBinding binding;
    private String defaultEmailAddress;
    private String emailAddress;
    private String defaultPhoneNumber;
    private String phoneNumber;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get username's information
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        defaultEmailAddress = sharedPreferences.getString("emailAddress", "");
        defaultPhoneNumber = sharedPreferences.getString("phoneNumber", "");
        emailAddress = defaultEmailAddress;
        phoneNumber = defaultPhoneNumber;

        // Set username and default contact info
        binding.editProfileName.setText(username);
        binding.editProfileEmailAddress.setText(defaultEmailAddress);
        binding.editProfilePhoneNumber.setText(defaultPhoneNumber);

        // Get and set user's email address
        if (Objects.equals(defaultEmailAddress, "Not available")) {
            db.collection("Player").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String queriedEmailAddress = document.getString("emailAddress");
                        if (queriedEmailAddress != null) {
                            emailAddress = queriedEmailAddress;
                        }
                        else{
                            emailAddress = "";
                        }
                        binding.editProfileEmailAddress.setText(emailAddress);
                    }
                }
            });
        }

        // Update email address
        binding.editProfileEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailAddress = s.toString().trim();
                if (!defaultEmailAddress.equals(emailAddress)){
                    displayUnsavedScreen();
                }
                else{
                    displaySavedScreen();
                }
            }
        });

        // Update phone number
        binding.editProfilePhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = s.toString().trim();
                if (!defaultPhoneNumber.equals(phoneNumber)){
                    displayUnsavedScreen();
                }
                else{
                    displaySavedScreen();
                }
            }
        });

        // Navigate back to the profile screen
        binding.editProfileButtonBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_profileFragment));

        // Update user's contact info on the database
        binding.editProfileButtonSave.setOnClickListener(v -> {
            db.collection("Player").document(username).update("emailAddress", emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    // Successfully update contact info!
                    if (task.isSuccessful()){
                        Toast.makeText(requireActivity(), "Updated successfully!", Toast.LENGTH_SHORT).show();
                        defaultEmailAddress = emailAddress;
                        defaultPhoneNumber = phoneNumber;
                        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE).edit();
                        editor.putString("emailAddress", emailAddress);
                        editor.putString("phoneNumber", phoneNumber);
                        editor.apply();
                        displaySavedScreen();
                    }

                    // Unsuccessfully update contact info!
                    else{
                        Toast.makeText(requireActivity(), "There has been an error! Updated unsuccessfully!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

        return view;

    }

    // Update UI when user's contact info is yet to be saved
    private void displayUnsavedScreen(){
        binding.editProfileButtonSave.setTextAppearance(requireActivity(), R.style.Button_Save);
        binding.editProfileButtonSave.setStrokeWidth(0);
        binding.editProfileButtonSave.setStrokeColor(null);
        binding.editProfileButtonSave.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.thistle));
        binding.editProfileButtonSave.setEnabled(true);
    }

    // Update UI when user's contact info is saved
    private void displaySavedScreen(){
        binding.editProfileButtonSave.setTextAppearance(R.style.Button_Save_Unclickable);
        binding.editProfileButtonSave.setStrokeWidth(Math.round(getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        binding.editProfileButtonSave.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.chinese_violet)));
        binding.editProfileButtonSave.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.editProfileButtonSave.setEnabled(false);
    }

}
