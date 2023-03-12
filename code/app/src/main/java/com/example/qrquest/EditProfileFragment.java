package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

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

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
//        String username = sharedPref.getString("username", "");
        String username = "UI5";

        userName.setText(username);

        phonenumber = "(999) - 999 - 999";
        phoneNumber.setText(phonenumber);

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

//        if (emailAddress.getEditableText().toString().trim() != emailaddress || phoneNumber.getEditableText().toString().trim() != phonenumber){
//            buttonSave = (MaterialButton) getLayoutInflater().inflate(R.layout.button_unclickable, null);
//        }
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), R.style.Button_Save_Unclickable);
        buttonSave = new MaterialButton(wrapper, null, 0);

//        buttonSave.setTextAppearance(R.style.Button_Save_Unclickable);

        buttonBack.setOnClickListener(v -> {Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_profileFragment);});

        return view;

    }
}