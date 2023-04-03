package com.example.qrquest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrquest.databinding.ProfileScreenBinding;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class defines the profile screen
 * @author Dang Viet Anh Dinh
 */
public class ProfileFragment extends Fragment {

    MainViewModel viewModel;
    private FirebaseFirestore db;
    ProfileScreenBinding binding;
    private HistoryAdapter adapter;
    boolean myProfile = false;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProfileScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.profile_screen_qr_codes);
        adapter = new HistoryAdapter(new HistoryAdapter.historyDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Get username and user view
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        myProfile = sharedPref.getBoolean("myProfile", false);
        if (myProfile) {
            username = sharedPref.getString("username", "");
        }
        else{
            username = sharedPref.getString("otherPlayer", "");
        }

        // Adding touch access to the recycler view
        if (myProfile) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                // Swipe to remove a QR Code from the account
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getLayoutPosition();
                    viewModel.deleteQR(db, username, position);
                    adapter.notifyItemRemoved(position);
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

        // Set profile username
        binding.profileScreenName.setText(username);

        // Initialize view model
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Set QR Code history for display
        viewModel.setHistory(db, username);

        // Set user view for display
        viewModel.setMyProfile(myProfile);

        // Get history to observe
        viewModel.getHistory().observe(requireActivity(), qrCodeHistories -> adapter.submitList(qrCodeHistories));

        // Get total score to observe
        viewModel.getTotalScore().observe(requireActivity(), integer -> binding.profileScreenScore.setText(String.valueOf(integer)));

        // Get total codes to observe
        viewModel.getTotalCodes().observe(requireActivity(), integer -> binding.profileScreenCode.setText(String.valueOf(integer)));

        // Get user view to observe
        viewModel.getMyProfile().observe(requireActivity(), aBoolean -> {
            if (aBoolean){
                binding.profileScreenButtonEdit.setVisibility(View.VISIBLE);
            }
            else{
                binding.profileScreenButtonEdit.setVisibility(View.INVISIBLE);
            }
            binding.profileScreenButtonEdit.setEnabled(aBoolean);
        });

        // Reverse sorting order
        binding.profileScreenButtonSort.setOnClickListener(v -> viewModel.reverseHistory());

        // Navigate back to the main screen or search screen
        binding.profileScreenButtonBack.setOnClickListener(v -> {
            boolean searching = sharedPref.getBoolean("searching", false);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("myProfile", false);
            editor.apply();
            if (myProfile) {
                if (searching) {
                    Navigation.findNavController(v).navigate(R.id.action_profileFragment2_to_searchFragment);
                }
                else{
                    Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mainFragment);
                }
            }
            else {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment2_to_searchFragment);
            }
        });

        // Navigate to edit profile screen
        binding.profileScreenButtonEdit.setOnClickListener(v ->Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_editProfileFragment));

        return view;

    }
}
