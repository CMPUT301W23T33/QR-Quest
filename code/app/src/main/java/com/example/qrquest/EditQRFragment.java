package com.example.qrquest;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qrquest.databinding.FragmentEditQrBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Locale;

public class EditQRFragment extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ArrayList<VPItem> arrayList;
    FragmentEditQrBinding binding;

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

        // Set up viewPager2
        int[] images = {R.drawable.qr_logo_big, R.drawable.qr_logo_big};


        arrayList = new ArrayList<>();
        for (int image : images) arrayList.add(new VPItem(image));

        VPAdapter vpAdapter = new VPAdapter(arrayList);
        binding.pager.setAdapter(vpAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText("")
        ).attach();

        return view;

    }
}
