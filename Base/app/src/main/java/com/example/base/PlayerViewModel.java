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
    private MutableLiveData<List<String>> mutableQueryDataSet;


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

    public void populateQueryData(){
        if (mutableQueryDataSet == null){
            this.mutableQueryDataSet = this.repository.getQueryString();
        }
    }

    // (Throwaway) fetch data
    public LiveData<List<String>> getDataset(){
        return this.mutableDataSet;
    }

    public LiveData<List<String>> getQueryDataset(){
        return this.mutableQueryDataSet;
    }

    public LiveData<List<String>> addQueryString(double score) {
        this.mutableQueryDataSet = this.repository.addQueryString(score);
        return this.mutableQueryDataSet;
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

    // (Throwaway) add data
    public LiveData<List<String>> removeAtPosition(int position){
        this.mutableDataSet = this.repository.removeAtPosition(position);
        return this.mutableDataSet;
    }

    public LiveData<List<String>> removeAtQueryPosition(int position){
        this.mutableQueryDataSet = this.repository.removeAtQueryPosition(position);
        return this.mutableQueryDataSet;
    }


}
