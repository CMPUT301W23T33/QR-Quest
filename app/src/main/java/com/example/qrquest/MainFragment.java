package com.example.qrquest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrquest.databinding.FragmentMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * This class defines the main screen
 * @author Thea Nguyen
 */
public class MainFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    FirebaseFirestore db;
    private GoogleMap map;
    private LocationManager manager;
    private String username;

    FragmentMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationPermissionRequest.launch(new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // initialize db
        db = FirebaseFirestore.getInstance();

        // Initialize view model
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // get username from Shared Preferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        username = sharedPref.getString("username", "");

        // Camera button
        binding.buttonCamera1.setOnClickListener(v -> {

            // Reset view model
            if (!MainViewModel.getRefreshPermission()) {
                viewModel.refreshHistory();
            }

            Intent intent = new Intent(requireActivity(), CameraActivity.class);
            startActivity(intent);
        });

        // Leaderboard button
        binding.buttonLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LeaderboardActivity.class);
            startActivity(intent);
        });


        // Navigate to the profile screen
        binding.profile.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_mainFragment_to_profileFragment));

        return view;
    }

    @SuppressLint("MissingPermission")
    ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
            .RequestMultiplePermissions(), result -> {
        Boolean fineLocationGranted = result.getOrDefault(
                android.Manifest.permission.ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,false);


        if ((fineLocationGranted != null && fineLocationGranted)
                || (coarseLocationGranted != null && coarseLocationGranted)) {
            checkLocationEnabled();

            // setup location manager
            manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } else {
            // erase local memories
            SharedPreferences sp = requireActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();

            // erase the account in firestore
            db.collection("Player").document(username)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("DELETE", "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("DELETE", "Error deleting document", e));

            // terminate the app
            requireActivity().moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    });

    /**
     * This method turns a string of hex color into a hsv color, which is applicable for changing the
     * markers' color on the map.
     * @param color a string of hexadecimal color (#000000)
     * @return a HSV color for map marker
     */
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    /**
     * This method checks if the location settings of the user is enabled or not.
     */
    private void checkLocationEnabled() {
        LocationManager manager1 = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = false;
        boolean networkEnabled = false;

        try {
            GPSEnabled = manager1.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = manager1.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception error) {
            error.printStackTrace();
        }

        if (!GPSEnabled || !networkEnabled) {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Enable GPS/Wifi Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", (dialog, which) ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Decline", null)
                    .show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            requireActivity(), R.raw.style_json));
            if (!success) {
                Log.e("STYLE", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("STYLE", "Can't find style. Error: ", e);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

        String locale = String.format(Locale.CANADA,"(%s, %s)", location.getLatitude(), location.getLongitude());
        Log.d("MainFragmentLocation", locale);

        // find the region
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(requireActivity());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(),1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String region = addresses.get(0).getAdminArea();
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE).edit();
        editor.putString("region", region);
        editor.apply();

        // set region of the user
        db.collection("Player").document(username)
                .update("region", region)
                .addOnSuccessListener(unused -> Log.d("UPDATE", "Successfully updated"))
                .addOnFailureListener(e -> Log.d("UPDATE", "Error updating document"));

        // set markers for nearby QR codes
        db.collection("main").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    double latitude = Double.parseDouble(String.valueOf(doc.get("latitude")));
                    double longitude = Double.parseDouble(String.valueOf(doc.get("longitude")));
                    float[] list = new float[1];
                    Location.distanceBetween(location.getLatitude(),
                            location.getLongitude(),
                            latitude, longitude, list);

                    // distance radius within 20km (nearby)
                    if (list[0] < 1000000000) {
                        LatLng latLng1 = new LatLng(latitude, longitude);
                        map.addMarker(new MarkerOptions()
                                .position(latLng1)
                                .title(String.valueOf(doc.get("name")))
                                .icon(getMarkerIcon("#CDB4DB")));
                    }
                }
            }
        });
        manager.removeUpdates(this);

        map.setOnMarkerClickListener(marker -> {
            double markerLatitude = marker.getPosition().latitude;
            double markerLongitude = marker.getPosition().longitude;

            db.collection("main").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        double qrLatitude = Double.parseDouble(String.valueOf(doc.get("latitude")));
                        double qrLongitude = Double.parseDouble(String.valueOf(doc.get("longitude")));

                        // get the corresponding qr code
                        if (markerLatitude == qrLatitude && markerLongitude == qrLongitude) {
                            String qrName = String.valueOf(doc.get("qrCode"));
                            int qrScore = Integer.parseInt(String.valueOf(doc.get("score")));

                            Bundle bundle = new Bundle();
                            if (doc.get("imagePath") != null) {
                                Uri qrUri = Uri.parse(String.valueOf(doc.get("imagePath")));
                                bundle.putString("uri", String.valueOf(qrUri));
                            }
                            bundle.putString("qrName", qrName);
                            bundle.putInt("qrScore", qrScore);
                            bundle.putString("latitude", String.valueOf(qrLatitude));
                            bundle.putString("longitude", String.valueOf(qrLongitude));

                            Intent intent = new Intent(getContext(), QRDisplayActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                }
            });
            return true;
        });



    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

}