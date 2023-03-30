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

    // Set up data for the leaderboard activity
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

    // Get the leaderboard to observe (top4 -> ...)
    public LiveData<ArrayList<Rank>> getLeaderboard(){return this.leaderboard;}

    // Get the user to observe
    public LiveData<Rank> getUser(){return this.user;}

    // Get the top 1st player to observe
    public LiveData<Rank> getFirst(){return this.first;}

    // Get the top 2nd player to observe
    public LiveData<Rank> getSecond(){return this.second;}

    // Get the top 3rd player to observe
    public LiveData<Rank> getThird(){return this.third;}

    // Get the type of leaderboard to observe
    public LiveData<Integer> getLeaderboardPosition(){return this.leaderboardPosition;}

    // Get the first leaderboard (highest scoring QR Codes) for display
    public void setFirstLeaderboard(FirebaseFirestore db, String username) {this.leaderboardRepository.setFirstLeaderboard(db, username);}

    // Get the second leaderboard (highest regional score QR Codes) for display
    public void setSecondLeaderboard(FirebaseFirestore db, String username, String region) {this.leaderboardRepository.setSecondLeaderboard(db, username, region);}

    // Get the third leaderboard (highest number of QR Codes owned) for display
    public void setThirdLeaderboard(FirebaseFirestore db, String username) {this.leaderboardRepository.setThirdLeaderboard(db, username);}

    // Get the last leaderboard (highest sum of QR Codes) for display
    public void setLastLeaderboard(FirebaseFirestore db, String username) {this.leaderboardRepository.setLastLeaderboard(db, username);}

    // Get the current type of leaderboard for display
    public void setLeaderboardPosition(int position){this.leaderboardRepository.setLeaderboardPosition(position);}

    // Allow the leaderboard to refresh
    public void refreshHistory(){
        this.leaderboardRepository.refreshHistory();
    }

}