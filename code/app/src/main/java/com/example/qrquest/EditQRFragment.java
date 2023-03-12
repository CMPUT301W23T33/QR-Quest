package com.example.qrquest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qrquest.databinding.FragmentEditQrBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class EditQRFragment extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ArrayList<VPItem> arrayList;
    FragmentEditQrBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditQrBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
