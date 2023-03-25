package com.example.qrquest;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrquest.databinding.FragmentQrDisplayBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Locale;

public class QRDisplayFragment extends Fragment {

    FragmentQrDisplayBinding binding;
    ArrayList<VPItem> arrayList;
    String uri;
    double latitude;
    double longitude;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQrDisplayBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = getArguments();
        assert bundle != null;

        binding.qrNameText.setText(bundle.getString("qrName"));
        binding.qrScoreText.setText(String.valueOf(bundle.getInt("qrScore")));

        if (bundle.getString("latitude") != null) {
            latitude = Double.parseDouble(bundle.getString("latitude"));
            longitude = Double.parseDouble(bundle.getString("longitude"));
            binding.qrLocationText.setText(String.format(Locale.CANADA,"(%.2f,%.2f)", latitude, longitude));
        }
        else {
            String noLocation = "None";
            binding.qrLocationText.setText(noLocation);
        }

        if (bundle.getString("uri") != null)
            uri = bundle.getString("uri");

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



        return view;
    }
}