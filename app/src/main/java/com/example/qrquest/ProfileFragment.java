package com.example.qrquest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
    private QRHistoryAdapter adapter;

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
        adapter = new QRHistoryAdapter(new QRHistoryAdapter.historyDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Get username
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
       String username = sharedPref.getString("username", "");
//          String username = "UI5";

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

        // Set profile username
        binding.profileScreenName.setText(username);

        // Initialize view model
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // set user profile QR Code history for display
        viewModel.setHistory(db, username);

        // Observe any changes to the history
        viewModel.getHistory().observe(requireActivity(), qrCodeHistories -> adapter.submitList(qrCodeHistories));

        // Observe any changes to the user statistic
        viewModel.getUserInfo().observe(requireActivity(), integers -> {
            binding.profileScreenScore.setText(String.valueOf(integers.get(0)));
            binding.profileScreenCode.setText(String.valueOf(integers.get(1)));
            adapter.notifyDataSetChanged();
        });

        // Reverse sorting order
        binding.profileScreenButtonSort.setOnClickListener(v -> {
            viewModel.reverseHistory();
            adapter.notifyDataSetChanged();
        });

        // Navigate back to the main screen
        binding.profileScreenButtonBack.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mainFragment));

        // Navigate to edit profile screen
        binding.profileScreenButtonEdit.setOnClickListener(v ->Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_editProfileFragment));

        return view;

    }
}
