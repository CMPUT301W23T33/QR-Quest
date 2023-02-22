package com.example.base;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {

    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    TextView emailAddress;
    Button next, signOut, removeAccount, addItem, removeItem;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    PlayerViewModel playerViewModel;

    // Retrieving credential data after signing in
    private final ActivityResultLauncher<Intent> signingIn = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
        @Override
        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
            onSignInResult(result);
        }
    });

    @Override
    public void onStart() {
        super.onStart();

        // Update UI if there is an existing signed-in account
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Update UI
            emailAddress.setText(firebaseUser.getEmail());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep track of authentication options
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        next = view.findViewById(R.id.next);
        signOut = view.findViewById(R.id.sign_out);
        removeAccount = view.findViewById(R.id.remove);
        emailAddress = view.findViewById(R.id.email_address_input);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        signInButton = view.findViewById(R.id.sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        addItem = view.findViewById(R.id.additem);
        removeItem = view.findViewById(R.id.removeitem);

        // Set up adapter
        final DisplayAdapter adapter = new DisplayAdapter(new DisplayAdapter.StringDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                playerViewModel.removeAtPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Get the view model under the scope of the activity
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
        playerViewModel.populateData();
        playerViewModel.getDataset().observe(requireActivity(), strings -> {
            adapter.submitList(strings);
        } );

        // (Throwaway) Add item to recycler view
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerViewModel.addString();
                adapter.notifyDataSetChanged();
            }
        });

        // (Throwaway) remove item from recycler view if possible
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerViewModel.removeString();
                adapter.notifyDataSetChanged();
            }
        });

        // Define sign-in request with respect to Firestore authentication
        BeginSignInRequest.Builder signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build());

        // Navigate to add word fragment
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addWordFragment);
            }
        });

        // Launch a new activity for signing in using Google account and Email&Password
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For privacy purpose, some users prefer to sign in using email, which corresponds to the UI wireframe
                ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                        .setAndroidPackageName("com.example.Base", true, null)
                        .setHandleCodeInApp(true)
                        .setUrl("https://google.com")
                        .build();

                // Create a list of providers for sign in options
                // We can also use Facebook by adding to the list new AuthUI.IdpConfig.FacebookBuilder().build(), etc ...
                // To add more providers, go to Firebase console -> Authentication -> Sign-in method -> Add new provider
                // Note that we can change any UI properties so that the UI matches the features we offer and user(s) can be added manually
                List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(), new AuthUI.IdpConfig.EmailBuilder()
                        .enableEmailLinkSignIn()
                        .setActionCodeSettings(actionCodeSettings)
                        .build());

                // Launching sign in activity
                Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build();
                signingIn.launch(signInIntent);
            }
        });

        // Sign out all existing accounts
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Update UI
                        emailAddress.setText("Your email address");
                    }
                });
            }
        });

        // Delete account from the Firestore database
        // Unfortunately, we cannot remove credential(s) from the sign-in options since it is deprecated
        // But We can manually do so through Google Account -> Security -> Password Manager
        removeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().delete(requireContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Update UI
                        emailAddress.setText("Your email address");
                    }
                });
            }
        });

        return view;
    }

    // Retrieving credential data after signing in
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result){
        IdpResponse idpResponse = result.getIdpResponse();
//        idpResponse.
        // Successful sign-in
        if (result.getResultCode() == RESULT_OK){
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            emailAddress.setText(firebaseUser.getEmail());
        }

        // Failed sign-in
        else{
            // Update UI
        }
    }

}