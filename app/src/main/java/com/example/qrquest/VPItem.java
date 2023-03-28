package com.example.qrquest;


import android.content.Context;

import androidx.fragment.app.Fragment;

public class VPItem {
    int imageId;
    String imageUri;

    Fragment fragment;

    public VPItem(int imageId) {
        this.imageId = imageId;
    }

    public VPItem(String imageUri) {this.imageUri = imageUri;}

    public VPItem(Fragment context, String imageUri) {
        this.fragment = context;
        this.imageUri = imageUri;
    }

    public VPItem(Fragment context, int imageId) {
        this.fragment = context;
        this.imageId = imageId;
    }
}
