package com.example.qrquest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * This class defines all activities related to using camera
 * @author: Dang Viet Anh Dinh
 */
public class Camera extends Fragment {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private Button buttonCaptureImage, buttonBack;
    private ImageCapture imageCapture;
    private ImageAnalysis imageAnalysis;
    private final BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build();

    ArrayList<String> barCodeRawValues = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.camera, container, false);

        previewView = view.findViewById(R.id.camera_preview_view);
        buttonCaptureImage = view.findViewById(R.id.camera_button_capture_image);
        buttonBack = view.findViewById(R.id.camera_button_back);

        // Create the camera provider instance
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(requireActivity());

        // Add a listener to the camera provider
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                startCameraX(cameraProvider);
            }
            catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

        // Request user permission on camera and data access
        if (!hasCameraPermission()) {
            requestPermission();
        }

        // Take a photo and save to project
        buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // Finish using camera and navigate back to add_word_fragment
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        // Unbind all previous settings
        cameraProvider.unbindAll();

        // Sets up the camera -> default camera is the back camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Add preview use case to view the camera
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Add image capture use case to capture image
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // There is a bug with the rotation degree of the preview use case
        // On physical devices, preview and image turn out normally, but on emulator, the preview use case is rotated by -90 degrees
        // Throwaway, rotate the image saved to vertical -> There can be a bug with the image capture use case
        imageCapture.setTargetRotation(Surface.ROTATION_90);

        // Add image analysis use case to analyze image
        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        // Analyze image
        imageAnalysis.setAnalyzer(getExecutor(), new ImageAnalysis.Analyzer() {
            @Override
            @ExperimentalGetImage
            public void analyze(@NonNull ImageProxy image) {
                Image mediaImage = image.getImage();
                if (mediaImage != null){
                    InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                    BarcodeScanner scanner = BarcodeScanning.getClient(options);

                    Task<List<Barcode>> result = scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            for (Barcode barcode : barcodes){

                                String rawValue = barcode.getRawValue();
                                String displayValue = barcode.getDisplayValue();

                                // The output steam is expanded so fast that there need to be constraints to not interfere
                                // in the application activity
                                // Currently working on a nicer ways around the API!
                                // ArrayList<String> barCodeRawValues is a throwaway
                                // We can just use whatever we have first, put it in the view model (we can drop it later)
                                // and then navigate to the next fragment
                                if (barCodeRawValues.size() == 0){
                                    barCodeRawValues.add(rawValue);
                                    Toast.makeText(requireActivity(), rawValue, Toast.LENGTH_SHORT).show();
                                }
                                if (!Objects.equals(rawValue, barCodeRawValues.get(barCodeRawValues.size() - 1))){
                                    barCodeRawValues.add(rawValue);
                                    Toast.makeText(requireActivity(), rawValue, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), "Image processed failed!" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            image.close();
                        }
                    });
                }
            }
        });

        // Bind all these use cases to the camera
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    // Take photos and save them to the application local storage
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
        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(photoFile).build(), getExecutor(), new ImageCapture.OnImageSavedCallback() {
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