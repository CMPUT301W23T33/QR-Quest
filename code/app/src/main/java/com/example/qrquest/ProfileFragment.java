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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This class defines the profile screen
 * @author Dang Viet Anh Dinh
 */
public class ProfileFragment extends Fragment {

    MainViewModel viewModel;
    private FirebaseFirestore db;
    private QRHistoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_screen, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.profile_screen_qr_codes);
        TextView userScore = view.findViewById(R.id.profile_screen_score);
        TextView userCodes = view.findViewById(R.id.profile_screen_code);
        TextView userName = view.findViewById(R.id.profile_screen_name);
        ImageButton buttonBack = view.findViewById(R.id.profile_screen_button_back);
        ImageButton buttonSort = view.findViewById(R.id.profile_screen_button_sort);
        ImageButton buttonEdit = view.findViewById(R.id.profile_screen_button_edit);
        adapter = new QRHistoryAdapter(new QRHistoryAdapter.historyDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Get username
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");
//        String username = "UI5"; -> Testing

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
        userName.setText(username);

        // Initialize view model
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // set user profile QR Code history for display
        viewModel.setHistory(db, username);

        // Observe any changes to the history
        viewModel.getHistory().observe(requireActivity(), qrCodeHistories -> {adapter.submitList(qrCodeHistories);});

        // Observe any changes to the user statistic
        viewModel.getUserInfo().observe(requireActivity(), integers -> {
            userScore.setText(String.valueOf(integers.get(0)));
            userCodes.setText(String.valueOf(integers.get(1)));
        });

        // Reverse sorting order
        buttonSort.setOnClickListener(v -> {
            viewModel.reverseHistory();
            adapter.notifyDataSetChanged();
        });

        // Navigate back to the main screen
        buttonBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mainFragment);});

        // Navigate to edit profile screen
        buttonEdit.setOnClickListener(v ->{Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_editProfileFragment);});

        return view;

    }
}