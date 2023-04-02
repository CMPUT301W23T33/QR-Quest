package com.example.qrquest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class defines the repository for displaying comment(s)
 * @author Dang Viet Anh Dinh
 */
public class QRDisplayRepository {

    // Comments general setup
    private static QRDisplayRepository qrDisplayRepository;
    private Boolean scannedData = false;
    private boolean commented = false;
    private final MutableLiveData<Boolean> scanned = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Comment>> comments = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Player>> players = new MutableLiveData<>();
    private String qrName;

    // Comments data
    private ArrayList<Comment> commentData = new ArrayList<>();

    // Players data
    private ArrayList<Player> playerData = new ArrayList<>();

    // Get a representative instance of the class
    public static QRDisplayRepository getInstance(){
        if (qrDisplayRepository == null){
            qrDisplayRepository = new QRDisplayRepository();
        }
        return qrDisplayRepository;
    }

    // Comments on the QR Code
    public MutableLiveData<ArrayList<Comment>> getComments(){return this.comments;}

    // If the user has scanned the QR Code
    public MutableLiveData<Boolean> getScanned(){return this.scanned;}

    // Players who scanned the QR Code
    public MutableLiveData<ArrayList<Player>> getPlayers(){return this.players;}

    // Get comments for display
    public void setComments(FirebaseFirestore db, String username, String qrName){
        this.qrName = qrName;
        db.collection("main")
                .whereEqualTo("qrCode", qrName)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Comment comment = new Comment(document.getString("username"), document.getString("comment"));
                            if (Objects.equals(comment.getUsername(), username)){
                                scannedData = true;
                                if (Objects.equals(comment.getComment().trim(), "") || comment.getComment() == null) {
                                    commented = false;
                                }
                                else{
                                    commented = true;
                                    commentData.add(0, comment);
                                }
                            }
                            else{
                                commentData.add(comment);
                            }
                        }
                        comments.setValue(commentData);
                    }
                });
    }

    // Set the view of the user to the QR Code for display
    public void setScanned(boolean hasScanned){
        this.scannedData = hasScanned;
        this.scanned.setValue(hasScanned);
    }

    // Set the players who scanned the QR Code for display
    public void setPlayers(FirebaseFirestore db, String qrName){
        db.collection("main")
                .whereEqualTo("hashedQRCode", qrName)
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            playerData.add(new Player(document.getString("username")));
                        }
                        Log.d("CheckDataQueried", String.valueOf(playerData.size()));
                        players.setValue(playerData);
                    }
                });
    }

    // Add comment if the user has scanned the QR Code
    public void addComment(FirebaseFirestore db, String username, String comment){
        if (this.scannedData) {
            updateDatabase(db, username, this.qrName, comment);
            updateScreen(username, comment);
        }
    }

    // Refresh data
    public void refreshHistory(){
        this.commentData.clear();
        this.comments.setValue(null);
        this.playerData.clear();
        this.players.setValue(null);
        this.qrName = "";
        this.scannedData = false;
        this.scanned.setValue(false);
        this.commented = false;
    }

    // Update database when adding a comment
    private void updateDatabase(FirebaseFirestore db, String username, String qrName, String comment){
        db.collection("main")
                .document(username + "_" + qrName)
                .update("comment", comment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("Database update", "Comment updated successfully!");
                    }
                });
    }

    // Update UI when adding a comment
    private void updateScreen(String username, String comment){
        this.comments.setValue(null);
        if (this.commented){
            this.commentData.set(0, new Comment(username, comment));
        }
        else{
            this.commentData.add(0, new Comment(username, comment));
            this.commented = true;
        }
        this.comments.setValue(this.commentData);
    }

}