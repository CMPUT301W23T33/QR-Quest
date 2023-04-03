package com.example.qrquest;


import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * This class represents an item in a ViewPager.
 * It can hold either an image id, a local URI, a cloud URI, a fragment or a bitmap.
 * @author Thea Nguyen
 */
public class VPItem {
    int imageId;
    String uri;
    boolean isCloud;
    Fragment fragment;
    Bitmap bitmap;

    /**
     * Constructs a new VPItem object with an image id.
     * @param imageId The id of the image to be displayed.
     */
    public VPItem(int imageId) {
        this.imageId = imageId;
    }
    /**
     * Constructs a new VPItem object with a local URI.
     * @param localUri The local URI of the image to be displayed.
     */
    public VPItem(String localUri) {this.uri = localUri;}

    /**
     * Constructs a new VPItem object with a cloud URI.
     * @param context The fragment context.
     * @param uri The cloud URI of the image to be displayed.
     * @param isCloud A flag indicating whether the image is stored in the cloud.
     */
    public VPItem(Fragment context, String uri, boolean isCloud) {
        this.fragment = context;
        this.uri = uri;
        this.isCloud = isCloud;
    }

    /**
     * Constructs a new VPItem object with a fragment and an image id.
     * @param context The fragment context.
     * @param imageId The id of the image to be displayed.
     */
    public VPItem(Fragment context, int imageId) {
        this.fragment = context;
        this.imageId = imageId;
    }

    /**
     * Constructs a new VPItem object with a fragment and a bitmap.
     * @param context The fragment context.
     * @param bitmap The bitmap to be displayed.
     */
    public VPItem(Fragment context, Bitmap bitmap) {
        this.fragment = context;
        this.bitmap = bitmap;
    }
}
