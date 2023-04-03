package com.example.qrquest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

/**
 * This class defines the view model for the leaderboard activity
 * @author Dang Viet Anh Dinh
 */
public class LeaderboardViewModel extends AndroidViewModel {

    // Leaderboard general setup
    private final LeaderboardRepository leaderboardRepository;
    private MutableLiveData<ArrayList<Rank>> leaderboard;
    private MutableLiveData<Rank> user, first, second, third;
    private MutableLiveData<Integer> leaderboardPosition;

    /**
     * This method defines the view model of leaderboard
     * @param application application state
     */
    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        this.leaderboardRepository = LeaderboardRepository.getInstance();
        this.leaderboard = this.leaderboardRepository.getLeaderboard();
        this.user = this.leaderboardRepository.getUser();
        this.first = this.leaderboardRepository.getFirst();
        this.second = this.leaderboardRepository.getSecond();
        this.third = this.leaderboardRepository.getThird();
        this.leaderboardPosition = this.leaderboardRepository.getLeaderboardPosition();
    }

    /**
     * This method retrieves the leaderboard (from top 4) to be displayed and observed
     * @return the current leaderboard
     */
    public LiveData<ArrayList<Rank>> getLeaderboard(){return this.leaderboard;}

    /**
     * This method retrieves the user  to be displayed and observed
     * @return the current user statistics on the leaderboard
     */
    public LiveData<Rank> getUser(){return this.user;}

    /**
     * This method retrieves the top 1 player statistics on the leaderboard to be displayed and observed
     * @return the current top 1 player on the leaderboard
     */
    public LiveData<Rank> getFirst(){return this.first;}

    /**
     * This method retrieves the top 2 player statistics on the leaderboard to be displayed and observed
     * @return the current top 2 player on the leaderboard
     */
    public LiveData<Rank> getSecond(){return this.second;}

    /**
     * This method retrieves the top 3 player statistics on the leaderboard to be displayed and observed
     * @return the current top 3 player on the leaderboard
     */
    public LiveData<Rank> getThird(){return this.third;}

    /**
     * This method retrieves the type of the currently displayed leaderboard to be observed
     * @return the type of the currently displayed leaderboard
     */
    public LiveData<Integer> getLeaderboardPosition(){return this.leaderboardPosition;}

    /**
     * This method populates data for the first leaderboard (Highest Score QR Code)
     * @param db Firestore database
     * @param username the user's username
     */
    public void setFirstLeaderboard(FirebaseFirestore db, String username) {this.leaderboardRepository.setFirstLeaderboard(db, username);}

    /**
     * This method populates data for the second leaderboard (Regional Highest Score QR Code)
     * @param db Firestore database
     * @param username the user's username
     * @param region the user's region
     */
    public void setSecondLeaderboard(FirebaseFirestore db, String username, String region) {this.leaderboardRepository.setSecondLeaderboard(db, username, region);}

    /**
     * This method populates data for the third leaderboard (Highest Number of Scanned QR Codes)
     * @param db Firestore database
     * @param username the user's username
     */
    public void setThirdLeaderboard(FirebaseFirestore db, String username) {this.leaderboardRepository.setThirdLeaderboard(db, username);}

    /**
     * This method populates data for the last leaderboard (Highest Total Score)
     * @param db Firestore database
     * @param username the user's username
     */
    public void setLastLeaderboard(FirebaseFirestore db, String username) {this.leaderboardRepository.setLastLeaderboard(db, username);}

    /**
     * This method updates the current type of leaderboard
     * @param position the current type of leaderboard (first, second, third, last)
     */
    public void setLeaderboardPosition(int position){this.leaderboardRepository.setLeaderboardPosition(position);}

    /**
     * This method refresh leaderboard history
     */
    public void refreshHistory(){
        this.leaderboardRepository.refreshHistory();
    }

}