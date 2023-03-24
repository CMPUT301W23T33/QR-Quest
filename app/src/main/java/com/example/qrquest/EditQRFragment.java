package com.example.qrquest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentEditQrBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * This class represents the Edit QR Screen and displays the hashed QR name, QR score, and QR
 * geo-locations (if any). This class also lets the user comments on the scanned QR code.
 * @author Thea Nguyen
 */
public class EditQRFragment extends Fragment {
    ArrayList<VPItem> arrayList;
    FragmentEditQrBinding binding;
    FirebaseFirestore db;
    CollectionReference qrCodeRef;
    String qrName;
    String uri;
    int qrScore;
    double latitude;
    double longitude;
    String username;
    String region;
    String comment;
    boolean newHighest = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditQrBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // take bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            qrName = bundle.getString("qrName");
            qrScore = bundle.getInt("qrScore");
            binding.qrNameText.setText(qrName);
            binding.qrScoreText.setText(String.valueOf(qrScore));

            if (bundle.getString("uri") != null)
                uri = bundle.getString("uri");

            if (bundle.getString("latitude") != null) {
                latitude = Double.parseDouble(bundle.getString("latitude"));
                longitude = Double.parseDouble(bundle.getString("longitude"));
                binding.qrLocationText.setText(String.format(Locale.CANADA,"(%.2f,%.2f)", latitude, longitude));
            }
            else {
                String noLocation = "None";
                binding.qrLocationText.setText(noLocation);
            }
        }

        // set up viewPager2
        if (uri != null) {
            String[] imageURIs = {uri};
            arrayList = new ArrayList<>();
            for (String imageURI : imageURIs) arrayList.add(new VPItem(imageURI));
        }
        // for demo (MUST BE MODIFIED AFTER HAVING A VISUAL REPRESENTATION)
        else {
            int[] imageIDs = {R.drawable.qr_logo_big, R.drawable.qr_logo_big};
            arrayList = new ArrayList<>();
            for (int imageID : imageIDs) arrayList.add(new VPItem(imageID));
        }

        VPAdapter vpAdapter = new VPAdapter(arrayList);
        binding.pager.setAdapter(vpAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText("")
        ).attach();

        // push to firestore db
        db = FirebaseFirestore.getInstance();
        qrCodeRef = db.collection("QR Code");

        // check button
        // MUST ADD THE IMAGE TAKEN BY USER HERE TO FIRESTORE STORAGE (SET WILL OVERWRITE THE
        // EXISTING DATA)

        // Get basic info for updating the database
        username = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE).getString("username", "");
//        username = "UI5";
        region = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE).getString("region", "");
        String documentName = username + "_" + qrName;
        Date date = new Date();

        // Updating to the database
        binding.buttonCheck.setOnClickListener(v -> {

            // Collection "QR Code"
            QRCode qrCode = new QRCode(qrName, qrScore, latitude, longitude);

            qrCodeRef.document(qrCode.getHashedQRCode())
                    .set(qrCode)
                    .addOnSuccessListener(unused -> Log.d("SET", "Added document successfully"))
                    .addOnFailureListener(e -> Log.d("SET", "Error adding document"));

            // Collection "main"
            comment = binding.commentText.getText().toString();
            Info info = new Info(comment, latitude, longitude, qrName, region, qrScore, date, username);

            Log.d("Document", documentName);
            db.collection("main").document(documentName).set(info).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("main", "Successful!");
                }
            });

            // Collection "Player"
            db.collection("Player").document(username).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String strHighestScore =
                            String.valueOf(task.getResult().get("highestScore", Integer.class));
                    int highestScore = Integer.parseInt(strHighestScore);
                    if (qrScore > highestScore){
                        newHighest = true;
                    }
                    updatePlayer();
                }
            });

            requireActivity().finish();
        });

        binding.buttonClose.setOnClickListener(v -> requireActivity().finish());
        return view;

    }

    private void updatePlayer(){
        if (newHighest){
            db.collection("Player").document(username)
                    .update("score", FieldValue.increment(qrScore),
                            "highestScore", qrScore,
                            "hasScanned", FieldValue.increment(1))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Log.d("Player - newHighest", "Successful!");
                        }
                    });
        }
        else{
            db.collection("Player").document(username)
                    .update("score", FieldValue.increment(qrScore),
                            "hasScanned", FieldValue.increment(1))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Log.d("Player", "Successful!");
                        }
                    });
        }
    }

}
