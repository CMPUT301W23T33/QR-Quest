package com.example.qrquest;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class Repository {

    private static Repository repository;
    private static double highestScore = 0;
    private ArrayList<QRCodeHistory> historyData = new ArrayList<>();
    private ArrayList<Double> userInfoData = new ArrayList<>(2);
    private MutableLiveData<ArrayList<QRCodeHistory>> history = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Double>> userInfo = new MutableLiveData<>();

    public static Repository getInstance(){
        if (repository == null){
            repository = new Repository();
        }
        return repository;
    }

    public MutableLiveData<ArrayList<QRCodeHistory>> getHistory(){return this.history;}

    public MutableLiveData<ArrayList<Double>> getUserInfo(){return this.userInfo;}

    public void setHistory(FirebaseFirestore db, String username) {
        db.collection("main").whereEqualTo("username", username).orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            if (highestScore == 0) {
                                highestScore = document.getDouble("score");
                            }
                            historyData.add(new QRCodeHistory(document.get("hashedQRCode", String.class), document.get("score", Double.class)));
                        }
                    }
                    db.collection("Player").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userInfoData.add(0, document.get("score", Double.class));
                                    userInfoData.add(1, document.get("hasScanned", Double.class));
                                }
                                userInfo.setValue(userInfoData);
                            }
                        }
                    });
                    history.setValue(historyData);
                }
            }
        });
    }

    public void deleteQR(FirebaseFirestore db, String username, int position){
        String qrCode = this.historyData.get(position).getQrCode();
        updateDatabase(db, username, qrCode);
        updateScreen(position);
    }

    public void reverseHistory(){
        Collections.reverse(this.historyData);
        this.historyData.add(new QRCodeHistory());
        this.history.setValue(this.historyData);
        this.historyData.remove(this.historyData.size() - 1);
        this.history.setValue(this.historyData);
    }

    private void updateDatabase(FirebaseFirestore db, String username, String hashedQRCode){
        db.collection("main").whereEqualTo("username", username).whereEqualTo("hashedQRCode", hashedQRCode).limit(1).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
                        double score = document.get("score", Double.class);
                        updateMain(db, document, hashedQRCode, score);
                        updatePlayer(db, username, score);
                        updateQRCode(db, hashedQRCode);
                    }
                }
            }
        });
    }

    private void updateScreen( int position){
        this.userInfoData.set(0, this.userInfoData.get(0) - this.historyData.get(position).getScore());
        this.userInfoData.set(1, this.userInfoData.get(1) - 1);
        this.historyData.remove(position);
        this.userInfo.setValue(this.userInfoData);
        this.history.setValue(this.historyData);
    }

    private void updatePlayer(FirebaseFirestore db, String username, double score) {
        db.collection("Player").document(username).update("score", FieldValue.increment(-score));
        db.collection("Player").document(username).update("hasScanned", FieldValue.increment(-1));
        if (userInfoData.get(1) > 1) {
            db.collection("main").whereEqualTo("username", username).orderBy("score", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Log.d("3", String.valueOf(task.getResult().size()));
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            double score = document.getDouble("score");
                            Log.d("4", String.valueOf(score));
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

    private void updateMain(FirebaseFirestore db, QueryDocumentSnapshot document, String username, double score) {
        db.collection("main").document(document.getId()).delete();
    }

}
