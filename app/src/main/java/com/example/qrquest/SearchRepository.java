package com.example.qrquest;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This class defines the repository for the search activity
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
    private String lastSearchedKeywordData = "";

    /**
     * This method retrieves the representative instance of the searching repository
     * @return the repository to populate data for searching players
     */
    public static SearchRepository getInstance(){
        if (searchRepository == null){
            searchRepository = new SearchRepository();
        }
        return searchRepository;
    }

    /**
     * This method retrieves the search results to be displayed and observed
     * @return the search results
     */
    public MutableLiveData<ArrayList<Rank>> getSearchResult(){return this.searchResult;}

    /**
     * This method retrieves the searching state to be displayed and observed
     * @return the searching state
     */
    public MutableLiveData<Boolean> getSearchingDone(){return this.searchingDone;}

    /**
     * This method retrieves the last searched keyword to be displayed and observed
     * @return the last searched keyword
     */
    public MutableLiveData<String> getLastSearchedKeyword(){return this.lastSearchedKeyword;}

    /**
     * This method queries the search results
     * @param db Firestore database
     * @param keyword the search keyword
     */
    public void setSearchResult(FirebaseFirestore db, String keyword){
        clearSearchData();
        if (keyword != null && !keyword.equals("")) {
            if (!keyword.equals(this.lastSearchedKeywordData)) {
                searchingDone.setValue(false);
                db.collection("Player")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getId().toLowerCase().contains(keyword)) {
                                        searchResultData.add(new Rank(document.getString("username"), document.get("score", Integer.class)));
                                    }
                                }
                                searchResult.setValue(searchResultData);
                                lastSearchedKeywordData = keyword;
                                lastSearchedKeyword.setValue(lastSearchedKeywordData);
                                searchingDone.setValue(true);
                            }
                        });
            }
            else{
                this.searchResult.setValue(this.searchResultData);
            }
        }
    }

    /**
     * This method retrieves the username of the specified player
     * @param position the position of the specified player on the list
     * @return the username of the chosen player
     */
    public String getPlayerName(int position){return this.searchResultData.get(position).getIdentifier();}

    // Clear previous search cache when there is a new search
    private void clearSearchData(){
        this.searchResultData = new ArrayList<>();
        this.searchResult.setValue(null);
        this.lastSearchedKeywordData = "";
        this.lastSearchedKeyword.setValue("");
    }

}