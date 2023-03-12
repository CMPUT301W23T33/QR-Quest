package com.example.qrquest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentEditQrBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

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
        binding.buttonCheck.setOnClickListener(v -> {
            QRCode qrCode = new QRCode(qrName, qrScore, latitude, longitude);
            qrCodeRef.document(qrCode.getHashedQRCode())
                    .set(qrCode)
                    .addOnSuccessListener(unused -> Log.d("SET", "Added document successfully"))
                    .addOnFailureListener(e -> Log.d("SET", "Error adding document"));
            requireActivity().finish();
        });

        binding.buttonClose.setOnClickListener(v -> requireActivity().finish());
        return view;

    }
}
