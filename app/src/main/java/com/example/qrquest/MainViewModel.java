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

    // Set up data for the main activity
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = MainRepository.getInstance();
        this.history = this.mainRepository.getHistory();
        this.totalScore = this.mainRepository.getTotalScore();
        this.totalCodes = this.mainRepository.getTotalCodes();
        this.myProfile = this.mainRepository.getMyProfile();
    }

    // Get the history to observe
    public LiveData<ArrayList<History>> getHistory(){
        return this.history;
    }

    // Get the total score to observe
    public LiveData<Integer> getTotalScore(){return this.totalScore;}

    // Get the total codes to observe
    public LiveData<Integer> getTotalCodes(){return this.totalCodes;}

    //
    public LiveData<Boolean> getMyProfile(){return this.myProfile;}

    // Get history for display
    public void setHistory(FirebaseFirestore db, String username) {
        if (getRefreshPermission()) {
            this.mainRepository.setHistory(db, username);
            refresh = false;
        }
    }

    //
    public void setMyProfile(boolean isMyProfile) {this.mainRepository.setMyProfile(isMyProfile);}

    // Delete a QR Code
    public void deleteQR(FirebaseFirestore db, String username, int position){
        this.mainRepository.deleteQR(db, username, position);
    }

    // Reverse sorting order
    public void reverseHistory(){
        this.mainRepository.reverseHistory();
    }

    public void refreshHistory(){
        refresh = true;
        this.mainRepository.refreshHistory();
    }

    public static boolean getRefreshPermission(){
        return refresh;
    }

}