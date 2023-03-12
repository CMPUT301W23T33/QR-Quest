package com.example.qrquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

/**
 * This class defines the camera activity
 * @author Dang Viet Anh Dinh
 */
public class CameraActivity extends AppCompatActivity {
    public NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_camera);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
    }

}