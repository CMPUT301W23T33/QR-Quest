package com.example.base;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class QueryFragment extends Fragment {

    private final String regionCANADA = "CANADA";
    Button back, query, customQuery;
    RecyclerView recyclerView;
    DisplayAdapter adapter;
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
        customQuery = view.findViewById(R.id.custome_query);

        // Set up adapter
         adapter = new DisplayAdapter(new DisplayAdapter.StringDiff());
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

        customQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addQRtoExistingUI();
//                addQRtoNewUI();
                queryHighestScoringQRCode(43.73);
//                queryRegionalHighestScoringQRCode("CANADA");
//                queryHasScanned();
//                queryTotalQRCodeScore();
//                queryPlayerQRCodeHistory("UI1");
//                queryQRCodeComments("QRCode3");
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicQuery();
            }
        });

        return view;
    }

    //
    private void basicQuery(){
        firebaseFirestore.collection("Player").document("Player 3").collection("History")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
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

    //
    private void queryForSingleInfo(){
        firebaseFirestore.collection("main").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //                        .whereEqualTo("region", "AB").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            QRCode qrCode = document.toObject(QRCode.class);
                            Info info = document.toObject(Info.class);
                            vm.addQueryString(info.getScore());
                            vm.addQueryString(info.getLatitude());
                            vm.addQueryString(info.getLongitude());
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(requireContext(), "Query Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Add a new QR Code scanned by an existing user
    private void addQRtoExistingUI(){

        // Add to "main" collection
        String comment = "comment21";
        double latitude = 34.075;
        double longitude = 84.212;
        String qrCode = "QRCode1";
        String hashedQRCode = qrCode;
        double score = 5.321;
        Date timeStamp = new Date();
        String uniqueIdentifier = "UI2";
        String username = "username2";
        Info info = new Info(comment, hashedQRCode, latitude, longitude, qrCode, regionCANADA, score, timeStamp, uniqueIdentifier, username);
        String documentName = uniqueIdentifier + "_" + qrCode;
        firebaseFirestore.collection("main").document(documentName).set(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(requireActivity(), "Transaction failed!", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
        });

        // Modify "Player" collection
        // Must check if the new QR Code score is the new highest score or not, but I'm lazy
        firebaseFirestore.collection("Player").document(uniqueIdentifier).update(
                "hasScanned", FieldValue.increment(1),
                "score", FieldValue.increment(info.getScore())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(requireActivity(), "Transaction successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Add a new QR Code scanned by a new user
    private void addQRtoNewUI(){

        // Add to "main" collection
        String comment = "comment43";
        String qrCode = "QRCode3";
        String hashedQRCode = qrCode;
        double latitude = -45.78;
        double longitude = 63.634;
        double score = 105.643;
        String newRegion = "US";
        Date timeStamp = new Date();
        String uniqueIdentifier = "UI4";
        String username = "username4";
        Info info = new Info(comment, hashedQRCode, latitude, longitude, qrCode, newRegion, score, timeStamp, uniqueIdentifier, username);
        String documentName = uniqueIdentifier + "_" + qrCode;
        firebaseFirestore.collection("main").document(documentName).set(info);

        // Create a new document in "Player" collection
        Player player = new Player(1, score, newRegion, score, info.getUniqueIdentifier(), info.getUsername());
        firebaseFirestore.collection("Player").document(uniqueIdentifier).set(player);

    }

    // Query for the highest scoring QR Code rank
    private void queryHighestScoringQRCode(double highestScore){

        // Querying for the database for the list in descending order
        firebaseFirestore.collection("main").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Rank> rankings = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            vm.addQueryString(document.get("score", Double.class));
                            rankings.add(new Rank(document.get("username", String.class), document.get("score", Double.class), highestScore));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    for (Rank ranking : rankings){
                        Log.d("Rank Index", String.format("%d. %s: %f", ranking.getRank(), ranking.getUsername(), ranking.getScore()));
                        Toast.makeText(requireContext(), String.format("%d. %s: %f", ranking.getRank(), ranking.getUsername(), ranking.getScore()), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("Highest Scoring QR Code Rank", String.format("%d: %f", rankings.get(0).getQueryRank(), rankings.get(0).getQueryScore()));
                }
            }
        });

    }

    // Query for the regional highest scoring QR Code rank
    // We need to create composite index to use query this one
    private void queryRegionalHighestScoringQRCode(String region){
        firebaseFirestore.collection("main").whereEqualTo("region", region).orderBy("score", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            vm.addQueryString(document.get("score", Double.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Query for the number of scanned QR Code rank
    private void queryHasScanned(){
        firebaseFirestore.collection("Player").orderBy("hasScanned", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            vm.addQueryString(document.get("hasScanned", Integer.class));
                            Toast.makeText(requireContext(), document.get("username", String.class), Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Query for the sum of QR Code score rank
    private void queryTotalQRCodeScore(){
        firebaseFirestore.collection("Player").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            vm.addQueryString(document.get("score", Double.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Query for Player's QR Code history
    private void queryPlayerQRCodeHistory(String uniqueIdentifier){
        firebaseFirestore.collection("main").whereEqualTo("uniqueIdentifier", uniqueIdentifier).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<PlayerQRCodeHistory> histories = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            histories.add(new PlayerQRCodeHistory(document.get("hashedQRCode", String.class), document.get("score", Double.class)));
                        }
                    }
                    for (PlayerQRCodeHistory history : histories){
                        Toast.makeText(requireContext(), history.getQrCode(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    // Query for QR Code's comments
    private void queryQRCodeComments(String hashedQRCode){
        firebaseFirestore.collection("main").whereEqualTo("hashedQRCode", hashedQRCode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<QRCodeComment> comments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            comments.add(new QRCodeComment(document.get("username", String.class), document.get("comment", String.class)));
                        }
                    }
                    for (QRCodeComment comment : comments){
                        Toast.makeText(requireContext(), comment.getComment(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}