package com.example.qrquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.json.JSONException;

import java.io.IOException;

/**
 * This Activity is used to host fragment navigation.
 * Its fragments consist of CreateAccountFragment, StartFragment.
 * @author Thea Nguyen
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // retrieve user data if any
        SharedPreferences sb = getSharedPreferences("sp", Context.MODE_PRIVATE);
        if (sb.contains("username")) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        try {
            Utilities.readJSON(getBaseContext(), "words.json");
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        // NavController navController = navHostFragment.getNavController();

    }
}