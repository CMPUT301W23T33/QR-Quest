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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrquest.databinding.CameraScreenBinding;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * This class defines the camera screen
 * @author Dang Viet Anh Dinh
 */
public class Camera extends Fragment {

    private CameraScreenBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ImageCapture imageCapture;
    private final BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build();
    private long start, end;
    ArrayList<String> barCodeRawValues = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CameraScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
        binding.cameraButtonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // Finish using camera and navigate back to the previous activity
        binding.cameraButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
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
    @NonNull
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(requireActivity());
    }

    // Set up camera
    private void startCameraX(@NonNull ProcessCameraProvider cameraProvider) {

        // Unbind all previous settings
        cameraProvider.unbindAll();

        // Sets up the camera -> default camera is the back camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Add preview use case to view the camera
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.cameraPreviewView.getSurfaceProvider());

        // Add image capture use case to capture image
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // Add image analysis use case to analyze image
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
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
                            for (Barcode barcode : barcodes) {

                                String rawValue = barcode.getRawValue();
                                //String displayValue = barcode.getDisplayValue();

                                // The output steam is expanded so rapidly that there need to be constraints to not interfere
                                // in the application activity
                                // Currently working on a nicer ways around the API!
                                // ArrayList<String> barCodeRawValues is a placeholder for the QR Code to be added to the view model
                                // for other fragments to use -> We can use just String in case when the list gets too large
                                // We can just use whatever we have first, put it in the view model (we can drop it later)
                                // and then navigate to the next fragment
                                if (barCodeRawValues.size() == 0) {
                                    start = System.currentTimeMillis();
                                    barCodeRawValues.add(rawValue);
                                    Toast.makeText(requireActivity(), rawValue, Toast.LENGTH_SHORT).show();
                                }

                                // Wait for at least 3 seconds before allowing another qr code to be scanned
                                end = System.currentTimeMillis();
                                if (end - start >= 3000) {
                                    start = System.currentTimeMillis();
                                    barCodeRawValues.add(rawValue);
                                    Toast.makeText(requireActivity(), rawValue, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Image processed failed!" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

        // Photo is saved at /storage/emulated/0/Android/data/com.example.qrquest/cache/Pictures (absolute path)
        // Go to Files -> Android -> data -> com.example.qrquest -> cache -> Pictures
        File photoFile = new File(photoFilePath);
        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(photoFile).build(), getExecutor(), new ImageCapture.OnImageSavedCallback() {
            // Save image to app storage (and navigate to the next screen if needed)
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // Navigate if needed (currently wait for the integration of other fragment)
                Toast.makeText(requireActivity(), "Photo has been saved successfully", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(requireActivity(), "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}