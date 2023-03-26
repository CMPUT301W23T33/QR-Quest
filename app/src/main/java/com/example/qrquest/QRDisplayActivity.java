package com.example.qrquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

/**
 * This Activity is used to host fragment navigation.
 * Its fragments consist of QRDisplayFragment.
 * @author Thea Nguyen
 */
public class QRDisplayActivity extends AppCompatActivity {

    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdisplay);

        Bundle bundle = getIntent().getExtras();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_display);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        fragment.setArguments(bundle);
    }
}