package com.example.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Repository {

    private static Repository repository;
    // Throwaway
    private double count = 1000f;
    private ArrayList<String> dataSet = new ArrayList<>();
    private MutableLiveData<List<String>> data = new MutableLiveData<>();

    /**
     * We don't really need this a class for repository, we just need a member, therefore just go for static to represent the class
     * We may want pass FirebaseFirestore in the ViewModel and then in here in a default constructor to interact with the Firestore database
     * If ever needed we would create a DAO for local database here as well
     * @return a static Repository
     */
    public static Repository getInstance(){
        if (repository == null){
            repository = new Repository();
        }
        return repository;
    }

    // (Throwaway)
    public MutableLiveData<List<String>> getString(){
        setString();
        this.data.setValue(dataSet);
        return this.data;
    }

    // (Throwaway) populate data
    private void setString(){
        dataSet.add("1");
        dataSet.add("10");
        dataSet.add("100");
    }

    // (Throwaway) add data
    public MutableLiveData<List<String>> addString(){
        dataSet.add(String.format("%.0f", count));
        count*= 10;
        this.data.setValue(dataSet);
        return this.data;
    }

    // (Throwaway) remove the last indexed data
    public MutableLiveData<List<String>> removeString(){
        if (dataSet.size() != 0) {
            dataSet.remove(dataSet.size() - 1);
            count /= 10;
            this.data.setValue(dataSet);
        }
        return this.data;
    }

    // (Throwaway) remove data at position p
    public MutableLiveData<List<String>> removeAtPosition(int p){
        dataSet.remove(p);
        this.data.setValue(dataSet);
        return this.data;
    }

}
