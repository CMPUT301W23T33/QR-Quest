package com.example.qrquest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

/**
 * This class defines the view model for displaying comment(s)
 * @author Dang Viet Anh Dinh
 */
public class QRDisplayViewModel extends AndroidViewModel {

    private static boolean refresh = true;
    private final QRDisplayRepository qrDisplayRepository;
    private MutableLiveData<ArrayList<Comment>> comments;
    private MutableLiveData<Boolean> commented;

    public QRDisplayViewModel(@NonNull Application application) {
        super(application);
        this.qrDisplayRepository = QRDisplayRepository.getInstance();
        this.comments = this.qrDisplayRepository.getComments();
        this.commented = this.qrDisplayRepository.getScanned();
    }

    // Get the comment(s) to observe
    public LiveData<ArrayList<Comment>> getComments(){return this.comments;}

    // Get if the user has scanned the QR Code to observe
    public LiveData<Boolean> getScanned(){return this.commented;}

    // Set the comments for display
    public void setComments(FirebaseFirestore db, String username, String qrName){
        if (getRefreshPermission()){
            this.qrDisplayRepository.setComments(db, username, qrName);
            refresh = false;
        }
    }

    // Add a comment
    public void addComment(FirebaseFirestore db, String username, String comment){
        this.qrDisplayRepository.addComment(db, username, comment);
    }

    // Allow the leaderboard to refresh
    public void refreshHistory(){
        refresh = true;
        this.qrDisplayRepository.refreshHistory();
    }

    // Get current refresh permission
    public static boolean getRefreshPermission(){
        return refresh;
    }

}