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

    private static QRDisplayRepository qrDisplayRepository;
    private ArrayList<QRCodeComment> commentData = new ArrayList<>();
    private Boolean commentedCheck = false;
    private MutableLiveData<Boolean> commented = new MutableLiveData<>();
    private MutableLiveData<ArrayList<QRCodeComment>> comments = new MutableLiveData<>();
    private String qrName;
    private int index = -1;

    public static QRDisplayRepository getInstance(){
        if (qrDisplayRepository == null){
            qrDisplayRepository = new QRDisplayRepository();
        }
        return qrDisplayRepository;
    }

    // Comments on the QR Code
    public MutableLiveData<ArrayList<QRCodeComment>> getComments(){return this.comments;}

    // If the user has scanned the QR Code
    public MutableLiveData<Boolean> getScanned(){return this.commented;}

    // Get comments for display
    public void setComments(FirebaseFirestore db, String username, String qrName){
        this.qrName = qrName;
        db.collection("main").whereEqualTo("username", username).whereEqualTo("qrCode", qrName).orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        QRCodeComment comment = new QRCodeComment(document.getString("username"), document.getString("comment"));
                        commentData.add(comment);
                        Log.d("Current Stack Size 1", String.valueOf(commentData.size()));
                        if (Objects.equals(comment.getUsername(), username)){
                            commentedCheck = true;
                            Log.d("Current Stack Size 2", String.valueOf(commentData.size()));
                            index = commentData.size() - 1;
                        }
                    }
                    comments.setValue(commentData);
                }
            }
        });
    }

    // Add comment if the user has scanned the QR Code
    public void addComment(FirebaseFirestore db, String username, String comment){
        if (this.commentedCheck) {
            updateDatabase(db, username, this.qrName, comment);
            updateScreen(username, comment);
        }
    }

    // Refresh data
    public void refreshHistory(){
        this.commentData.clear();
        this.comments.setValue(null);
        this.qrName = "";
        this.commentedCheck = false;
        this.index = -1;
    }

    // Update database when adding a comment
    private void updateDatabase(FirebaseFirestore db, String username, String qrName, String comment){
        db.collection("main").document(username + "_" + qrName).update("comment", comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("Database update", "Comment updated successfully!");
                }
            }
        });
    }

    // Update UI when adding a comment
    private void updateScreen(String username, String comment){
        this.comments.setValue(null);
        this.commentData.set(this.index, new QRCodeComment(username, comment));
        this.comments.setValue(this.commentData);
    }

}
