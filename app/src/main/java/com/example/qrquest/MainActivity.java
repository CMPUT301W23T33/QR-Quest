package com.example.qrquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;

/**
 * This class represents the main screen of the app. It prompts for users' location permission and
 * displays a map if granted.
 * @author Thea Nguyen
 */
public class MainActivity extends AppCompatActivity {

    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_main);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if (bundle.getBoolean("profile"))
               navController.navigate(R.id.action_mainFragment_to_profileFragment);
        }
    }

}