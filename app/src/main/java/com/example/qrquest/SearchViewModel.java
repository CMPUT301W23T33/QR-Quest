package com.example.qrquest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

/**
 * This class defines the view model for the search activity
 * @author Dang Viet Anh Dinh
 */
public class SearchViewModel extends AndroidViewModel {

    // Search general setup
    private final SearchRepository searchRepository;
    private MutableLiveData<ArrayList<Rank>> searchResult;
    private MutableLiveData<Boolean> searchingDone;
    private MutableLiveData<String> lastSearchedKeyword;

    // Set up data for the search activity
    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.searchRepository = SearchRepository.getInstance();
        this.searchResult = this.searchRepository.getSearchResult();
        this.searchingDone = this.searchRepository.getSearchingDone();
        this.lastSearchedKeyword = this.searchRepository.getLastSearchedKeyword();
    }

    // Get the search result to observe
    public LiveData<ArrayList<Rank>> getSearchResult(){return this.searchResult;}

    //
    public LiveData<Boolean> getSearchingDone(){return this.searchingDone;}

    //
    public LiveData<String> getLastSearchedKeyword(){return this.lastSearchedKeyword;}

    // Get the last leaderboard (highest sum of QR Codes) for display
    public void setSearchResult(FirebaseFirestore db, String keyword) {this.searchRepository.setSearchResult(db, keyword);}

}