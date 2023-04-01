package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import androidx.navigation.Navigation;
import androidx.test.core.app.ApplicationProvider;

import com.example.qrquest.databinding.EditProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.*;


public class EditProfileTest {

    private EditProfileFragment editProfileFragment;
    private EditProfileBinding binding;
    private SharedPreferences sharedPref;
    private FirebaseFirestore db;

    @Before
    public void setUp() {
        editProfileFragment = new EditProfileFragment();
        binding = EditProfileBinding.inflate(LayoutInflater.from(ApplicationProvider.getApplicationContext()));
        //sharedPref = Mockito.mock(SharedPreferences.class);
        //db = Mockito.mock(FirebaseFirestore.class);
        editProfileFragment.db = db;
        editProfileFragment.binding = binding;
        initMocks(this);
    }

    @Test
    public void testOnCreateView() throws InterruptedException {
        String username = "testUsername";
        String emailAddress = "testEmail@example.com";

        // Mock shared preferences
        when(sharedPref.getString("username", "")).wait(Long.parseLong(username));
        Context context = ApplicationProvider.getApplicationContext();
        when(context.getSharedPreferences("sp", Context.MODE_PRIVATE)).thenReturn(sharedPref);

        // Verify that the UI elements are set correctly
        editProfileFragment.onCreateView(LayoutInflater.from(ApplicationProvider.getApplicationContext()), null, null);
        assertEquals(username, binding.editProfileName.getText().toString());
        assertEquals(emailAddress, binding.editProfileEmailAddress.getText().toString());
    }


    @Test
    public void testNavigateBackToProfileFragment() throws InterruptedException {
        View view = Mockito.mock(View.class);
        when(String.valueOf(view.getId())).wait(R.id.action_editProfileFragment_to_profileFragment);
        editProfileFragment.onCreateView(LayoutInflater.from(ApplicationProvider.getApplicationContext()), null, null);
        editProfileFragment.binding.editProfileButtonBack.performClick();
        verify(view).findViewById(R.id.action_editProfileFragment_to_profileFragment);
        verify(Navigation.findNavController(view)).navigate(R.id.action_editProfileFragment_to_profileFragment);
    }

}
