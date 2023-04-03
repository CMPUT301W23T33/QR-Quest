package com.example.qrquest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrquest.databinding.ProfileScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

/**
 * This class defines the profile screen
 * @author Dang Viet Anh Dinh
 */
public class ProfileFragment extends Fragment {

    MainViewModel viewModel;
    private FirebaseFirestore db;

    ProfileScreenBinding binding;
    private HistoryAdapter adapter;

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
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.profile_screen_qr_codes);
        adapter = new HistoryAdapter(new HistoryAdapter.historyDiff());
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                List<History> list = adapter.getCurrentList();
                History history = list.get(position);

                db.collection("main")
                        .whereEqualTo("qrCode", history.getQrCode())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("hashString", doc.get("hashedQRCode", String.class));
                                        bundle.putString("qrName", doc.get("qrCode", String.class));
                                        bundle.putInt("qrScore", Integer.parseInt(String.valueOf(doc.get("score"))));
                                        bundle.putString("latitude", String.valueOf(doc.get("latitude")));
                                        bundle.putString("longitude", String.valueOf(doc.get("longitude")));
                                        if (doc.get("uri") != null)
                                            bundle.putString("uri", doc.get("imagePath", String.class));

                                        bundle.putBoolean("profile", true);
                                        Intent intent = new Intent(getContext(), QRDisplayActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Get username
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");

        // Adding touch access to the recycler view
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

        // Get history to observe
        viewModel.getHistory().observe(requireActivity(), qrCodeHistories -> adapter.submitList(qrCodeHistories));

        // Get total score to observe
        viewModel.getTotalScore().observe(requireActivity(), integer -> binding.profileScreenScore.setText(String.valueOf(integer)));

        // Get total codes to observe
        viewModel.getTotalCodes().observe(requireActivity(), integer -> binding.profileScreenCode.setText(String.valueOf(integer)));

        // Reverse sorting order
        binding.profileScreenButtonSort.setOnClickListener(v -> viewModel.reverseHistory());

        // Navigate back to the main screen
        binding.profileScreenButtonBack.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mainFragment));

        // Navigate to edit profile screen
        binding.profileScreenButtonEdit.setOnClickListener(v ->Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_editProfileFragment));

        return view;

    }
}
