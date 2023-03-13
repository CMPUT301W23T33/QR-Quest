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

    private static boolean refresh = true;
    private MainRepository mainRepository;
    private MutableLiveData<ArrayList<QRCodeHistory>> history;
    private MutableLiveData<ArrayList<Integer>> userInfo;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = MainRepository.getInstance();
        this.history = this.mainRepository.getHistory();
        this.userInfo = this.mainRepository.getUserInfo();
    }

    // Get QR Code history to observe
    public LiveData<ArrayList<QRCodeHistory>> getHistory(){
        return this.history;
    }

    // Get user info to observe
    public LiveData<ArrayList<Integer>> getUserInfo(){
        return this.userInfo;
    }

    // Get history for display
    public void setHistory(FirebaseFirestore db, String username) {
        if (refresh) {
            this.mainRepository.setHistory(db, username);
            refresh = false;
        }
    }

    // Delete a QR Code
    public void deleteQR(FirebaseFirestore db, String username, int position){
        this.mainRepository.deleteQR(db, username, position);
    }

    // Reverse sorting order
    public void reverseHistory(){
        this.mainRepository.reverseHistory();
    }

    // (Unfinished) reset updating permission
    public void refreshHistory(){
        refresh = true;
    }

}