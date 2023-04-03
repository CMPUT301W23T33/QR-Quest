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
    private MutableLiveData<Boolean> scanned;
    private MutableLiveData<ArrayList<Player>> players;

    /**
     * This method defines the view model of QR display screen
     * @param application application state
     */
    public QRDisplayViewModel(@NonNull Application application) {
        super(application);
        this.qrDisplayRepository = QRDisplayRepository.getInstance();
        this.comments = this.qrDisplayRepository.getComments();
        this.scanned = this.qrDisplayRepository.getScanned();
        this.players = this.qrDisplayRepository.getPlayers();
    }

    /**
     * This method retrieves the QR Code comments to be displayed and observed
     * @return the QR Code comments
     */
    public LiveData<ArrayList<Comment>> getComments(){return this.comments;}

    /**
     * This method retrieves state if the user has scanned the QR Code to be displayed and observed
     * @return the QR Code history
     */
    public LiveData<Boolean> getScanned(){return this.scanned;}

    /**
     * This method retrieves the players who has scanned the QR Code to be displayed and observed
     * @return the players who has scanned the QR Code
     */
    public LiveData<ArrayList<Player>> getPlayers(){return this.players;}

    /**
     * This method populates the comments about the QR Code
     * @param db Firestore database
     * @param username the user's username
     * @param qrName the QR Code name
     */
    public void setComments(FirebaseFirestore db, String username, String qrName){
        if (getRefreshPermission()){
            this.qrDisplayRepository.setComments(db, username, qrName);
            refresh = false;
        }
    }

    /**
     * This method sets the view of the user with respect to the QR Code
     * @param hasScanned the view of the user with respect to the QR Code
     */
    public void setScanned(boolean hasScanned){this.qrDisplayRepository.setScanned(hasScanned);}

    /**
     * This method populates the players who has scanned the QR Code
     * @param db Firestore database
     * @param qrName the QR Code name
     */
    public void setPlayers(FirebaseFirestore db, String qrName){this.qrDisplayRepository.setPlayers(db, qrName);}

    /**
     * This method adds/overrides a comment
     * @param db Firestore database
     * @param username the user's username
     * @param comment the comment made by the user
     */
    public void addComment(FirebaseFirestore db, String username, String comment){this.qrDisplayRepository.addComment(db, username, comment);}

    /**
     * This method refreshes the QR Code queried data
     */
    public void refreshHistory(){
        refresh = true;
        this.qrDisplayRepository.refreshHistory();
    }

    /**
     * This method retrieves the QR Code refresh permission
     * @return the QR Code history refresh permission
     */
    public static boolean getRefreshPermission(){
        return refresh;
    }

}