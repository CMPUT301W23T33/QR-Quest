package com.example.qrquest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ApplicationViewModel extends AndroidViewModel {

    private static boolean refresh = true;
    private MutableLiveData<Double> userScore;
    private Repository repository;
    private MutableLiveData<ArrayList<QRCodeHistory>> history;
    private MutableLiveData<ArrayList<Double>> userInfo;

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();
        this.history = this.repository.getHistory();
        this.userInfo = this.repository.getUserInfo();
    }

    public LiveData<ArrayList<QRCodeHistory>> getHistory(){
        return this.history;
    }

    public LiveData<ArrayList<Double>> getUserInfo(){
        return this.userInfo;
    }

    public void setHistory(FirebaseFirestore db, String username) {
        if (refresh) {
            this.repository.setHistory(db, username);
            refresh = false;
        }
    }

    public void deleteQR(FirebaseFirestore db, String username, int position){
        this.repository.deleteQR(db, username, position);
    }

    public void reverseHistory(){
        this.repository.reverseHistory();
    }

}
