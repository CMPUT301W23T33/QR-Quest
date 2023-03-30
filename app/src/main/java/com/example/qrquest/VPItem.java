package com.example.qrquest;


import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class VPItem {
    int imageId;
    String uri;
    boolean isCloud;
    Fragment fragment;
    Bitmap bitmap;

    public VPItem(int imageId) {
        this.imageId = imageId;
    }

    public VPItem(String localUri) {this.uri = localUri;}

    public VPItem(Fragment context, String uri, boolean isCloud) {
        this.fragment = context;
        this.uri = uri;
        this.isCloud = isCloud;
    }

    public VPItem(Fragment context, int imageId) {
        this.fragment = context;
        this.imageId = imageId;
    }

    public VPItem(Fragment context, Bitmap bitmap) {
        this.fragment = context;
        this.bitmap = bitmap;
    }
}
