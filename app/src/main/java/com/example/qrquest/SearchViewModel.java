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

    /**
     * This method defines the view model of search screen
     * @param application application state
     */
    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.searchRepository = SearchRepository.getInstance();
        this.searchResult = this.searchRepository.getSearchResult();
        this.searchingDone = this.searchRepository.getSearchingDone();
        this.lastSearchedKeyword = this.searchRepository.getLastSearchedKeyword();
    }

    /**
     * This method retrieves the search results to be displayed and observed
     * @return the search results
     */
    public LiveData<ArrayList<Rank>> getSearchResult(){return this.searchResult;}

    /**
     * This method retrieves the searching state to be displayed and observed
     * @return the searching state
     */
    public LiveData<Boolean> getSearchingDone(){return this.searchingDone;}

    /**
     * This method retrieves the last searched keyword to be displayed and observed
     * @return the last searched keyword
     */
    public LiveData<String> getLastSearchedKeyword(){return this.lastSearchedKeyword;}

    /**
     * This method retrieves the username of the specified player
     * @param position the position of the specified player on the list
     * @return the username of the chosen player
     */
    public String getPlayerName(int position){return this.searchRepository.getPlayerName(position);}

    /**
     * This method populates the search results
     * @param db Firestore database
     * @param keyword the search keyword
     */
    public void setSearchResult(FirebaseFirestore db, String keyword) {this.searchRepository.setSearchResult(db, keyword);}

}