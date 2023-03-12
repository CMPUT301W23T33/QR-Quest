package com.example.qrquest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrquest.databinding.CameraScreenBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * This class defines the camera screen
 * @author Dang Viet Anh Dinh
 * @author Thea Nguyen
 */
public class Camera extends Fragment {

    private CameraScreenBinding binding;
    private View view;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ImageCapture imageCapture;
    private long start, end;
    private String rawValue;
    private final BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build();
    ArrayList<String> barCodeRawValues = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CameraScreenBinding.inflate(inflater, container, false);
        view = binding.getRoot();

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

        binding.cameraButtonCaptureImage.setEnabled(false);

        // Take a photo and save to project
        binding.cameraButtonCaptureImage.setOnClickListener(v -> takePhoto());

        // Finish using camera and navigate back to the previous activity
        binding.cameraButtonBack.setOnClickListener(v -> requireActivity().finish());

        return view;

    }

    /**
     * This method checks for user's permission on using the camera.
     * @return True if all necessary permissions for camera usage are granted, else False
     */
    private boolean hasCameraPermission(){
        return ContextCompat.checkSelfPermission(requireActivity(),
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This method requests permissions from users (if any)
     */
    private void requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                10);
    }

    /**
     * This method creates an executor on the main thread.
     * @return an executor on the main thread
     */
    @NonNull
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(requireActivity());
    }

    /**
     * This methods initializes the camera once all necessary permissions are granted. It also
     * analyzes the picture to detect QR Codes (if any).
     * @param cameraProvider A singleton which can be used to bind the lifecycle of cameras to
     *                       any LifecycleOwner within an application's process
     */
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
                    InputImage inputImage = InputImage.fromMediaImage(mediaImage,
                            image.getImageInfo().getRotationDegrees());
                    BarcodeScanner scanner = BarcodeScanning.getClient(options);

                    scanner.process(inputImage).addOnSuccessListener(barcodes -> {
                        // if a barcode is detected, then enable camera button
                        if (barcodes.size() > 0) {
                            binding.cameraButtonCaptureImage.setEnabled(true);
                            binding.cameraButtonCaptureImage.setBackgroundTintList(
                                    ColorStateList.valueOf(Color.parseColor("#CDB4DB")));
                            binding.cameraButtonCaptureImage.setImageTintList(
                                    ColorStateList.valueOf(Color.parseColor("#CDB4DB")));
                        }
                        // else, disable camera button
                        else {
                            binding.cameraButtonCaptureImage.setEnabled(false);
                            binding.cameraButtonCaptureImage.setBackgroundTintList(
                                    ColorStateList.valueOf(Color.parseColor("#735D78")));
                            binding.cameraButtonCaptureImage.setImageTintList(
                                    ColorStateList.valueOf(Color.parseColor("#735D78")));
                        }

                        rawValue = "";
                        if (barcodes.size() > 0) {
                            rawValue = barcodes.get(barcodes.size() - 1).getRawValue();
                            Log.d("Scanning", rawValue);
                        }

                        // Duration between duration of each qr code scanned
                        if (barCodeRawValues.size() == 0) {
                            start = System.currentTimeMillis();
                            barCodeRawValues.add(rawValue);
                        }
                        // Wait for >= 3 seconds before allowing another qr code to be scanned
                        end = System.currentTimeMillis();
                        if (end - start >= 3000) {
                            start = System.currentTimeMillis();
                            barCodeRawValues.add(rawValue);
                        }
                    })
                    .addOnFailureListener(e -> binding.cameraButtonCaptureImage.setEnabled(false))
                    .addOnCompleteListener(task -> image.close());
                }
            }
        });
        // Bind all these use cases to the camera
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    /**
     * This method takes the photos and save them to the application local storage.
     */
    private void takePhoto() {
        File photoDir = new File(requireActivity().getExternalCacheDir() + "/Pictures");
        if (!photoDir.exists())
            //noinspection ResultOfMethodCallIgnored
            photoDir.mkdir();
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

        // Photo is saved at /storage/emulated/0/Android/data/com.example.qrquest/cache/Pictures (absolute path)
        // Go to Files -> Android -> data -> com.example.qrquest -> cache -> Pictures
        File photoFile = new File(photoFilePath);
        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Navigation.findNavController(view).navigate(R.id.action_camera_to_QRDetectedFragment);

                // PASSING THE RAW VALUE HERE (WOULD BE NICE IF WE CAN IMPLEMENT VIEW-MODEL)

            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(requireActivity(),
                        "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}