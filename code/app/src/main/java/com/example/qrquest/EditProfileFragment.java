package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This class defines the edit fragment screen
 * @author Dang Viet Anh Dinh
 */
public class EditProfileFragment extends Fragment {

    FirebaseFirestore db;

    String emailaddress, phonenumber;
    MaterialButton buttonSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile, container, false);

        ImageButton buttonBack = view.findViewById(R.id.edit_profile_button_back);
        TextView userName = view.findViewById(R.id.edit_profile_name);
        TextInputEditText emailAddress = view.findViewById(R.id.edit_profile_email_address);
        TextInputEditText phoneNumber = view.findViewById(R.id.edit_profile_phone_number);
        buttonSave = view.findViewById(R.id.edit_profile_button_save);
        LinearLayout linearLayout = view.findViewById(R.id.edit_profile_alert);

        // Get username
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");
//        String username = "UI5"; -> Testing

        userName.setText(username);

        // Set user's phone number
//        phoneNumber.setText(phonenumber);

        // Get and set user's email address
        db.collection("Player").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        emailaddress = document.getString("emailAddress");
                        emailAddress.setText(emailaddress);
                    }
                }
            }
        });

        // Navigate back to the profile screen
        buttonBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_profileFragment);});

        return view;

    }
}