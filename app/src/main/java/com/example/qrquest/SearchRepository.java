package com.example.qrquest;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This class defines the repository for the leaderboard activity
 * @author Dang Viet Anh Dinh
 */
public class SearchRepository {

    // Search general setup
    private static SearchRepository searchRepository;
    private final MutableLiveData<ArrayList<Rank>> searchResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> searchingDone = new MutableLiveData<>();
    private final MutableLiveData<String> lastSearchedKeyword = new MutableLiveData<>();

    // Search data
    private ArrayList<Rank> searchResultData = new ArrayList<>();

    // Get a representative instance of the class
    public static SearchRepository getInstance(){
        if (searchRepository == null){
            searchRepository = new SearchRepository();
        }
        return searchRepository;
    }

    // Get search result
    public MutableLiveData<ArrayList<Rank>> getSearchResult(){return this.searchResult;}

    // Get searching state
    public MutableLiveData<Boolean> getSearchingDone(){return this.searchingDone;}

    // Get the last searched keyword
    public MutableLiveData<String> getLastSearchedKeyword(){return this.lastSearchedKeyword;}

    // Set search result for display
    public void setSearchResult(FirebaseFirestore db, String keyword){
        clearSearchData();
        if (keyword != null && !keyword.equals("")) {
            searchingDone.setValue(false);
            db.collection("Player").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().toLowerCase().contains(keyword)) {
                            searchResultData.add(new Rank(document.getString("username"), document.get("score", Integer.class)));
                        }
                    }
                    searchResult.setValue(searchResultData);
                    lastSearchedKeyword.setValue(keyword);
                    searchingDone.setValue(true);
                    Log.d("Just Updated", String.valueOf(true));
                }
            });
        }
    }

    // Clear previous search cache when there is a new search
    private void clearSearchData(){
        searchResultData = new ArrayList<>();
        this.searchResult.setValue(null);
    }

}