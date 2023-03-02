package com.example.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ImageViewFragment extends Fragment {

    private Button back, setImage, getImage, uploadImage;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_image_view, container, false);

        back = view.findViewById(R.id.back);
        setImage = view.findViewById(R.id.set_image_local);
        imageView = view.findViewById(R.id.image_display);
        getImage = view.findViewById(R.id.get_image);
        uploadImage = view.findViewById(R.id.upload_image);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        // Upload an image from /storage/emulated/0/Android/data/com.example.base/cache/Pictures (absolute path) to Firebase Storage
        // Go to Files -> Device -> Android -> data -> com.example.base -> cache -> Pictures
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File local = new File(requireActivity().getExternalCacheDir() + "/Pictures/timestamp.jpg");
                if (local.exists()){
                    Uri file = Uri.fromFile(local);
                    StorageReference timestamp = storageReference.child("images/" + file.getLastPathSegment());
//                    StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();
//                    UploadTask uploadTask = timestamp.putFile(file, metadata);
                    UploadTask uploadTask = timestamp.putFile(file);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(requireContext(), "Image not uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            Toast.makeText(requireContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Download an image from Firebase Storage to /storage/emulated/0/Android/data/com.example.base/cache/Pictures (absolute path)
        // Go to Files -> Device -> Android -> data -> com.example.base -> cache -> Pictures
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StorageReference reference = storageReference.child("images/timestamp.jpg");
                File dir = new File(requireActivity().getExternalCacheDir() + "/Pictures");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                reference.getFile(new File(requireContext().getExternalCacheDir() + "/Pictures/image.jpg"))
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(requireContext(), "Image downloaded successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "Image not downloaded!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Display an image from /storage/emulated/0/Android/data/com.example.base/cache/Pictures (absolute path)
        // Go to Files -> Device -> Android -> data -> com.example.base -> cache -> Pictures
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(requireActivity().getExternalCacheDir() + "/Pictures/timestamp.jpg");
                if (file.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    imageView.setRotation(90);
                }
            }
        });

        // Navigate back to the add word fragment
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_imageViewFragment_to_addWordFragment);
            }
        });

        return view;

    }
}