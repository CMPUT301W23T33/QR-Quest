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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.guieffect.qual.UI;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    ApplicationViewModel viewModel;
    private FirebaseFirestore db;
    private HistoryAdapter adapter;

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
        adapter = new HistoryAdapter(new HistoryAdapter.historyDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
//        String username = sharedPref.getString("username", "");
        String username = "UI5";

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                viewModel.deleteQR(db, username, position);
                adapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        userName.setText(username);

        viewModel = new ViewModelProvider(requireActivity()).get(ApplicationViewModel.class);
        viewModel.setHistory(db, username);

        viewModel.getHistory().observe(requireActivity(), qrCodeHistories -> {adapter.submitList(qrCodeHistories);});

        viewModel.getUserInfo().observe(requireActivity(), doubles -> {
            double score = doubles.get(0);
            if (score >= 0.0001) {
                userScore.setText(String.format("%.4f", doubles.get(0)));
            }
            else{
                userScore.setText("0");
            }
            userCodes.setText(String.format("%.0f", doubles.get(1)));
        });

        buttonSort.setOnClickListener(v -> {
            viewModel.reverseHistory();
            adapter.notifyDataSetChanged();
        });

        buttonBack.setOnClickListener(v -> {Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mainFragment);});

        buttonEdit.setOnClickListener(v ->{Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_editProfileFragment);});

        return view;

    }
}