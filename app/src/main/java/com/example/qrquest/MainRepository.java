package com.example.qrquest;


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

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class defines the repository for the main activity
 * @author Dang Viet Anh Dinh
 */
public class MainRepository {

    // History general setup
    private static MainRepository mainRepository;
    private static int highestScore = 0;
    private final MutableLiveData<ArrayList<History>> history = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalCodes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> myProfile = new MutableLiveData<>();

    // History data
    private final ArrayList<History> historyData = new ArrayList<>();

    // Total score data
    private Integer totalScoreData = 0;

    // Total codes data
    private Integer totalCodesData = 0;

    /**
     * This method retrieves the representative instance of the main repository
     * @return the repository to populate data for the user profile
     */
    public static MainRepository getInstance(){
        if (mainRepository == null){
            mainRepository = new MainRepository();
        }
        return mainRepository;
    }

    /**
     * This method retrieves the QR Code history to be displayed and observed
     * @return the QR Code history
     */
    public MutableLiveData<ArrayList<History>> getHistory(){return this.history;}

    /**
     * This method retrieves the total score of the user to be displayed and observed
     * @return the current total score of the user
     */
    public MutableLiveData<Integer> getTotalScore(){return this.totalScore;}

    /**
     * This method retrieves the total number of scanned QR Codes of the user to be displayed and observed
     * @return the current total number of scanned QR Code of the user
     */
    public MutableLiveData<Integer> getTotalCodes(){return this.totalCodes;}

    /**
     * This method retrieves the view of the user on the profile
     * @return the view of the user on the current profile
     */
    public MutableLiveData<Boolean> getMyProfile(){return this.myProfile;}

    /**
     * This methods queries the QR Code history for display
     * @param db Firestore database
     * @param username the current username of the profile
     */
    public void setHistory(FirebaseFirestore db, String username) {
        db.collection("main")
                .whereEqualTo("username", username)
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                if (highestScore == 0) {
                                    highestScore = document.get("score", Integer.class);
                                }
                                historyData.add(new History(document.get("hashedQRCode", String.class),document.get("qrCode", String.class), document.get("score", Integer.class)));
                            }
                        }
                        history.setValue(historyData);

                        db.collection("Player")
                                .whereEqualTo("username", username)
                                .get()
                                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    totalScoreData = document.get("score", Integer.class);
                                    totalCodesData = document.get("hasScanned", Integer.class);
                                }
                                totalScore.setValue(totalScoreData);
                                totalCodes.setValue(totalCodesData);
                            }
                        });
                    }
                });
    }

    /**
     * This method sets the view of the user on the current profile
     * @param isMyProfile the view of the user on the current profile
     */
    public void setMyProfile(Boolean isMyProfile){
        this.myProfile.setValue(isMyProfile);
    }

    /**
     * This method deletes a QR Code from the profile specified by the user
     * @param db Firestore database
     * @param username the current username of the profile
     * @param position the position of the QR COde
     */
    public void deleteQR(FirebaseFirestore db, String username, int position){
        String qrCode = this.historyData.get(position).getQrCode();
        update(db, username, qrCode);
        updateScreen(position);
    }

    /**
     * This method refreshes the QR Code history of the profile
     */
    public void refreshHistory(){
        this.historyData.clear();
        this.history.setValue(null);
        this.totalScore.setValue(0);
        this.totalCodes.setValue(0);
        highestScore = 0;
    }

    /**
     * This method reverses the QR Code history sorting order
     */
    public void reverseHistory(){
        this.history.setValue(null);
        Collections.reverse(this.historyData);
        this.history.setValue(this.historyData);
    }

    // Update database
    private void update(FirebaseFirestore db, String username, String qrName){
        db.collection("main")
                .whereEqualTo("username", username)
                .whereEqualTo("qrCode", qrName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                int score = document.get("score", Integer.class);
                                updateMain(db, document);
                                updatePlayer(db, username, score);
                                updateQRCode(db, qrName);
                            }
                        }
                    }
                });
    }

    // Update screen
    private void updateScreen(int position){
        this.totalScoreData-= this.historyData.get(position).getScore();
        this.totalCodesData-= 1;
        this.totalScore.setValue(this.totalScoreData);
        this.totalCodes.setValue(this.totalCodesData);
        this.historyData.remove(position);
        this.history.setValue(this.historyData);
    }

    // Update "Player" collection
    private void updatePlayer(FirebaseFirestore db, String username, int score) {
        db.collection("Player").document(username).update("score", FieldValue.increment(-score));
        db.collection("Player").document(username).update("hasScanned", FieldValue.increment(-1));
        if (totalCodesData > 0) {
            db.collection("main")
                    .whereEqualTo("username", username)
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int score1 = document.get("score", Integer.class);
                                db.collection("Player").document(username).update("highestScore", score1);
                            }
                        }
                    });
        }
        else {
            db.collection("Player").document(username).update("highestScore", 0);
        }

    }

    // Update "QR Code" collection
    private void updateQRCode(FirebaseFirestore db, String qrName){
        db.collection("main")
                .whereEqualTo("qrCode", qrName)
                .count()
                .get(AggregateSource.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().getCount() == 0){
                            db.collection("QR Code").document(qrName).delete();
                        }
                    }
                });
    }

    // Update "main" collection
    private void updateMain(FirebaseFirestore db, QueryDocumentSnapshot document) {
        db.collection("main").document(document.getId()).delete();
    }

}
