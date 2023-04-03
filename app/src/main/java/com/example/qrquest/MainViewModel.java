package com.example.qrquest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class defines the view model for the main activity
 * @author Dang Viet Anh Dinh
 */
public class MainViewModel extends AndroidViewModel {

    // History general setup
    private static boolean refresh = true;
    private MainRepository mainRepository;
    private MutableLiveData<ArrayList<History>> history;
    private MutableLiveData<Integer> totalScore;
    private MutableLiveData<Integer> totalCodes;
    private MutableLiveData<Boolean> myProfile;

    /**
     * This method defines the view model of main screen
     * @param application application state
     */
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = MainRepository.getInstance();
        this.history = this.mainRepository.getHistory();
        this.totalScore = this.mainRepository.getTotalScore();
        this.totalCodes = this.mainRepository.getTotalCodes();
        this.myProfile = this.mainRepository.getMyProfile();
    }

    /**
     * This method retrieves the QR Code history to be displayed and observed
     * @return the QR Code history
     */
    public LiveData<ArrayList<History>> getHistory(){
        return this.history;
    }

    /**
     * This method retrieves the total score of the user to be displayed and observed
     * @return the current total score of the user
     */
    public LiveData<Integer> getTotalScore(){return this.totalScore;}

    /**
     * This method retrieves the total number of scanned QR Codes of the user to be displayed and observed
     * @return the current total number of scanned QR Code of the user
     */
    public LiveData<Integer> getTotalCodes(){return this.totalCodes;}

    /**
     * This method retrieves the view of the user on the profile
     * @return the view of the user on the current profile
     */
    public LiveData<Boolean> getMyProfile(){return this.myProfile;}

    /**
     * This methods populates the QR Code history for display
     * @param db Firestore database
     * @param username the current username of the profile
     */
    public void setHistory(FirebaseFirestore db, String username) {
        if (getRefreshPermission()) {
            this.mainRepository.setHistory(db, username);
            refresh = false;
        }
    }

    /**
     * This method sets the view of the user on the current profile
     * @param isMyProfile the view of the user on the current profile
     */
    public void setMyProfile(boolean isMyProfile) {this.mainRepository.setMyProfile(isMyProfile);}

    /**
     * This method deletes a QR Code from the profile specified by the user
     * @param db Firestore database
     * @param username the current username of the profile
     * @param position the position of the QR COde
     */
    public void deleteQR(FirebaseFirestore db, String username, int position){
        this.mainRepository.deleteQR(db, username, position);
    }

    /**
     * This method reverses the QR Code history sorting order
     */
    public void reverseHistory(){
        this.mainRepository.reverseHistory();
    }

    /**
     * This method refreshes the QR Code history of the profile
     */
    public void refreshHistory(){
        refresh = true;
        this.mainRepository.refreshHistory();
    }

    /**
     * This method retrieves the QR Code history refresh permission
     * @return the QR Code history refresh permission
     */
    public static boolean getRefreshPermission(){
        return refresh;
    }

}