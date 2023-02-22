package com.example.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private Repository repository;
    // Throwaway
    private MutableLiveData<List<String>> mutableDataSet;


//    private MutableLiveData<LinkedList<Player>> playerList;
//    private MutableLiveData<LinkedList<QRCode>> QRCodeList;
//    private MutableLiveData<LinkedList<Player>> QRCodeRank;
//    private MutableLiveData<LinkedList<Player>> regionalRank;
//    private MutableLiveData<LinkedList<Player>> scoreRank;
//    private MutableLiveData<LinkedList<Player>> scannedRank;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();
    }

    // (Throwaway) populate data
    public void populateData(){
        if (mutableDataSet == null){
            this.mutableDataSet = this.repository.getString();
        }
    }

    // (Throwaway) fetch data
    public LiveData<List<String>> getDataset(){
        return this.mutableDataSet;
    }

    // (Throwaway) add data
    public LiveData<List<String>> addString(){
        this.mutableDataSet = this.repository.addString();
        return this.mutableDataSet;
    }

    // (Throwaway) add data
    public LiveData<List<String>> removeString(){
        this.mutableDataSet = this.repository.removeString();
        return this.mutableDataSet;
    }

    public LiveData<List<String>> removeAtPosition(int position){
        this.mutableDataSet = this.repository.removeAtPosition(position);
        return this.mutableDataSet;
    }

//    public LiveData<LinkedList<Player>> getPlayerList(){
//        return playerList;
//    }
//
//    public LiveData<LinkedList<QRCode>> getQRCodeList(){
//        return QRCodeList;
//    }
//
//    public LiveData<LinkedList<Player>> getQRCodeRank(){
//        return QRCodeRank;
//    }
//
//    public LiveData<LinkedList<Player>> getRegionalRank(){
//        return regionalRank;
//    }
//
//    public LiveData<LinkedList<Player>> getScoreRank(){
//        return scoreRank;
//    }
//
//    public LiveData<LinkedList<Player>> getScannedRank(){
//        return scannedRank;
//    }

}
