package com.example.qrquest;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class defines the repository for the main activity
 * @author Dang Viet Anh Dinh
 */
public class MainRepository {

    private static MainRepository mainRepository;
    private static int highestScore = 0;
    private ArrayList<QRCodeHistory> historyData = new ArrayList<>();
    private ArrayList<Integer> userInfoData = new ArrayList<>(2);
    private MutableLiveData<ArrayList<QRCodeHistory>> history = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Integer>> userInfo = new MutableLiveData<>();

    public static MainRepository getInstance(){
        if (mainRepository == null){
            mainRepository = new MainRepository();
        }
        return mainRepository;
    }

    // User's QR Code history
    public MutableLiveData<ArrayList<QRCodeHistory>> getHistory(){return this.history;}

    // User's score and scanned number
    public MutableLiveData<ArrayList<Integer>> getUserInfo(){return this.userInfo;}

    // Get user's QR Code history and set up for display
    public void setHistory(FirebaseFirestore db, String username) {
        db.collection("main").whereEqualTo("username", username).orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            if (highestScore == 0) {
                                highestScore = document.get("score", Integer.class);
                            }
                            historyData.add(new QRCodeHistory(document.get("hashedQRCode", String.class), document.get("score", Integer.class)));
                        }
                    }
                    history.setValue(historyData);

                    db.collection("Player").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userInfoData.add(0, document.get("score", Integer.class));
                                    userInfoData.add(1, document.get("hasScanned", Integer.class));
                                }
                                userInfo.setValue(userInfoData);
                            }
                        }
                    });
                }
            }
        });
    }

    // Delete a QR Code
    public void deleteQR(FirebaseFirestore db, String username, int position){
        String qrCode = this.historyData.get(position).getQrCode();
        update(db, username, qrCode);
        updateScreen(position);
    }

    // Refresh data
    public void refreshHistory(){
        this.userInfoData.set(0, 0);
        this.userInfoData.set(1, 0);
        this.historyData.clear();
        this.history.setValue(null);
        this.userInfo.setValue(this.userInfoData);
        highestScore = 0;
    }

    // Reverse sorting order
    public void reverseHistory(){
        this.history.setValue(null);
        Collections.reverse(this.historyData);
        this.history.setValue(this.historyData);
    }

    // Update database
    private void update(FirebaseFirestore db, String username, String hashedQRCode){
        db.collection("main").whereEqualTo("username", username).whereEqualTo("hashedQRCode", hashedQRCode).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
                        int score = document.get("score", Integer.class);
                        updateMain(db, document);
                        updatePlayer(db, username, score);
                        updateQRCode(db, hashedQRCode);
                    }
                }
            }
        });
    }

    // Update screen
    private void updateScreen(int position){
        this.userInfoData.set(0, this.userInfoData.get(0) - this.historyData.get(position).getScore());
        this.userInfoData.set(1, this.userInfoData.get(1) - 1);
        this.userInfo.setValue(this.userInfoData);
        this.historyData.remove(position);
        this.history.setValue(this.historyData);
    }

    // Update "Player" collection
    private void updatePlayer(FirebaseFirestore db, String username, int score) {
        db.collection("Player").document(username).update("score", FieldValue.increment(-score));
        db.collection("Player").document(username).update("hasScanned", FieldValue.increment(-1));
        if (userInfoData.get(1) > 0) {
            db.collection("main").whereEqualTo("username", username).orderBy("score", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int score = document.get("score", Integer.class);
                            db.collection("Player").document(username).update("highestScore", score);
                        }
                    }
                }
            });
        }
        else {
            db.collection("Player").document(username).update("highestScore", 0);
        }

    }

    // Update "QR Code" collection
    private void updateQRCode(FirebaseFirestore db, String hashedQRCode){
        db.collection("main").whereEqualTo("hashedQRCode", hashedQRCode).count().get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getCount() == 0){
                        db.collection("QR Code").document(hashedQRCode).delete();
                    }
                }
            }
        });
    }

    // Update "main" collection
    private void updateMain(FirebaseFirestore db, QueryDocumentSnapshot document) {
        db.collection("main").document(document.getId()).delete();
    }

}
