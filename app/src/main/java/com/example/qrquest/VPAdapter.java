package com.example.qrquest;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {
    private final ArrayList<VPItem> arrayList;

    // constructor
    public VPAdapter(ArrayList<VPItem> arrayList) {
        this.arrayList = arrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.qr_picture);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.vp_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VPItem item = arrayList.get(position);
        if (item.isCloud) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference imageReference = storageReference.child(item.uri);
            GlideApp.with(item.fragment).load(imageReference).into(holder.imageView);
        }
        else if (item.uri != null) holder.imageView.setImageURI(Uri.parse(item.uri));
        else holder.imageView.setImageResource(item.imageId);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}