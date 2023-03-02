package com.example.base;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class QueryFragment extends Fragment {

    Button back, query;
    RecyclerView recyclerView;
    PlayerViewModel vm;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference1, collectionReference2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference1 = firebaseFirestore.collection("Player");
        collectionReference2 = firebaseFirestore.collection("Leaderboard");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_query, container, false);

        back = view.findViewById(R.id.button_back);
        recyclerView = view.findViewById(R.id.rv);
        query = view.findViewById(R.id.query);

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
                vm.removeAtQueryPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Get the view model under the scope of the activity
        vm = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
        vm.populateQueryData();
        vm.getQueryDataset().observe(requireActivity(), strings -> {
            adapter.submitList(strings);
        } );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_queryFragment_to_addWordFragment);
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Player").document("Player 3").collection("History")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            QRCode qrCode = document.toObject(QRCode.class);
                                            vm.addQueryString(qrCode.getScore());
                                        }
                                        else{
                                            Toast.makeText(requireContext(), "Query Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}