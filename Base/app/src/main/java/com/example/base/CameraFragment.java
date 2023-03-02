package com.example.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraFragment extends Fragment {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private Button takePhoto, back;
    private ImageCapture imageCapture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);


        previewView = view.findViewById(R.id.viewFinder);
        takePhoto = view.findViewById(R.id.image_capture_button);
        back = view.findViewById(R.id.camera_to_add_word_fragment);

        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(requireActivity());
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

        // Request user permission on camera and data access
        if (!hasCameraPermission()) {
            requestPermission();
        }

        // Take a photo and save to project
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // Finish using camera and navigate back to add_word_fragment
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_cameraFragment_to_addWordFragment);
            }
        });

        return view;

    }

    // Check for camera access
    private boolean hasCameraPermission(){
        return ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // Request for camera and data access
    private void requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                10);
    }

    // Create an executor on the main thread
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(requireActivity());
    }

    // Set up camera
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    // Take photos and save them to the project
    private void takePhoto() {
        File photoDir = new File(requireActivity().getExternalCacheDir() + "/Pictures");
        if (!photoDir.exists()) {
            photoDir.mkdir();
        }
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + "timestamp" + ".jpg";

        // Photo is saved at /storage/emulated/0/Android/data/com.example.base/cache/Pictures (absolute path)
        // Go to Files -> Device -> Android -> data -> com.example.base -> cache -> Pictures
        File photoFile = new File(photoFilePath);

        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(requireActivity(), "Photo has been saved successfully", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(requireActivity(), "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}