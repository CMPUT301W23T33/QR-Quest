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

    private static boolean refresh = true;
    private final LeaderboardRepository leaderboardRepository;
    private MutableLiveData<ArrayList<Rank>> leaderboard;
    private MutableLiveData<ArrayList<Rank>> userAndTopPlayers;
    private MutableLiveData<Integer> leaderboardPosition;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        this.leaderboardRepository = LeaderboardRepository.getInstance();
        this.leaderboard = this.leaderboardRepository.getLeaderboard();
        this.userAndTopPlayers = this.leaderboardRepository.getUserAndTopPlayers();
        this.leaderboardPosition = this.leaderboardRepository.getLeaderboardPosition();
    }

    // Get the leaderboard to observe (top4 -> ...)
    public LiveData<ArrayList<Rank>> getLeaderboard(){return this.leaderboard;}

    // Get user and top players (1st, 2nd and 3rd) to observe
    public LiveData<ArrayList<Rank>> getUserAndTopPlayers(){return this.userAndTopPlayers;}

    // Get the type of leaderboard to observe
    public LiveData<Integer> getLeaderboardPosition(){return this.leaderboardPosition;}

    // Get leaderboard for display
    public void setLeaderboard(){
        if (getRefreshPermission()){
            this.leaderboardRepository.setLeaderboard();
            refresh = false;
        }
    }

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
        refresh = true;
        this.leaderboardRepository.refreshHistory();
    }

    // Get current refresh permission
    public static boolean getRefreshPermission(){
        return refresh;
    }

}