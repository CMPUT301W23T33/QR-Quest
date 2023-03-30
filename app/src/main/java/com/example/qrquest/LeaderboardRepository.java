package com.example.qrquest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class defines the repository for the leaderboard activity
 * @author Dang Viet Anh Dinh
 */
public class LeaderboardRepository {

    private static LeaderboardRepository leaderboardRepository;
    private ArrayList<Rank> firstLeaderboardData = new ArrayList<>();
    private ArrayList<Rank> secondLeaderboardData = new ArrayList<>();
    private ArrayList<Rank> thirdLeaderboardData = new ArrayList<>();
    private ArrayList<Rank> lastLeaderboardData = new ArrayList<>();
    private MutableLiveData<ArrayList<Rank>> leaderboard = new MutableLiveData<>();
    private ArrayList<Rank> firstUserAndTopPlayersData = new ArrayList<>();
    private ArrayList<Rank> secondUserAndTopPlayersData = new ArrayList<>();
    private ArrayList<Rank> thirdUserAndTopPlayersData = new ArrayList<>();
    private ArrayList<Rank> lastUserAndTopPlayersData = new ArrayList<>();
    private ArrayList<Rank> cache = new ArrayList<>();
    private MutableLiveData<ArrayList<Rank>> userAndTopPlayer = new MutableLiveData<>();
    private Integer position = 0;
    private boolean fetchFirstLeaderboard, fetchSecondLeaderboard, fetchThirdLeaderboard, fetchLastLeaderboard;
    private MutableLiveData<Integer> leaderboardPosition = new MutableLiveData<>();

    public static LeaderboardRepository getInstance(){
        if (leaderboardRepository == null){
            leaderboardRepository = new LeaderboardRepository();
        }
        return leaderboardRepository;
    }

    // Get leaderboard
    public MutableLiveData<ArrayList<Rank>> getLeaderboard(){return this.leaderboard;}

    // Get user and top players (1st, 2nd, 3rd) ranking information
    public MutableLiveData<ArrayList<Rank>> getUserAndTopPlayers(){return this.userAndTopPlayer;}

    // Get type of leaderboard
    public MutableLiveData<Integer> getLeaderboardPosition(){return this.leaderboardPosition;}

    //
    public void setLeaderboard(){
        Rank rank = new Rank();
        for (int index =0; index < 4; index++){
            this.cache.add(rank);
            this.firstUserAndTopPlayersData.add(rank);
            this.secondUserAndTopPlayersData.add(rank);
            this.thirdUserAndTopPlayersData.add(rank);
            this.lastUserAndTopPlayersData.add(rank);
        }
    }

    //
    public void setFirstLeaderboard(FirebaseFirestore db, String username){
        this.leaderboard.setValue(null);
        this.userAndTopPlayer.setValue(cache);
        if (!this.fetchFirstLeaderboard) {
            db.collection("main").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean found = false;
                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HighestScoreRank temp = new HighestScoreRank(document.getString("username"), document.get("score", Integer.class));
                            if (Objects.equals(temp.getIdentifier(), username) & !found) {
                                firstUserAndTopPlayersData.set(3, temp);
                                found = true;
                            }
                            if (index < 3) {
                                firstUserAndTopPlayersData.set(index, temp);
                                index++;
                            }
                            else{
                                firstLeaderboardData.add(temp);
                            }
                            Log.d("LeaderboardQuery", String.valueOf(document));
                        }
                        Log.d("Leaderboard 111", String.valueOf(firstLeaderboardData.size()));
                        leaderboard.setValue(firstLeaderboardData);
                        userAndTopPlayer.setValue(firstUserAndTopPlayersData);
                        fetchFirstLeaderboard = true;
                        Log.d("Leaderboard 112", String.valueOf(firstLeaderboardData.size()));
                    }
                }
            });
        }
        else{
            Log.d("Leaderboard 121", String.valueOf(firstLeaderboardData.size()));
            this.leaderboard.setValue(this.firstLeaderboardData);
            this.userAndTopPlayer.setValue(this.firstUserAndTopPlayersData);
            Log.d("Leaderboard 122", String.valueOf(firstLeaderboardData.size()));
        }
    }

    //
    public void setSecondLeaderboard(FirebaseFirestore db, String username, String region){
        this.leaderboard.setValue(null);
        this.userAndTopPlayer.setValue(cache);
        if (!this.fetchSecondLeaderboard) {
            db.collection("main").whereEqualTo("region", region).orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean found = false;
                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RegionalHighestScoreRank temp = new RegionalHighestScoreRank(document.getString("username"), document.get("score", Integer.class));
                            if (Objects.equals(temp.getIdentifier(), username) & !found) {
                                secondUserAndTopPlayersData.set(3, temp);
                                found = true;
                            }
                            if (index < 3) {
                                secondUserAndTopPlayersData.set(index, temp);
                                index++;
                            }
                            else{
                                secondLeaderboardData.add(temp);
                            }
                        }
                        Log.d("Leaderboard 211", String.valueOf(secondLeaderboardData.size()));
                        leaderboard.setValue(secondLeaderboardData);
                        userAndTopPlayer.setValue(secondUserAndTopPlayersData);
                        fetchSecondLeaderboard = true;
                        Log.d("Leaderboard 212", String.valueOf(secondLeaderboardData.size()));
                    }
                }
            });
        }
        else{
            Log.d("Leaderboard 221", String.valueOf(secondLeaderboardData.size()));
            this.leaderboard.setValue(this.secondLeaderboardData);
            this.userAndTopPlayer.setValue(this.secondUserAndTopPlayersData);
            Log.d("Leaderboard 222", String.valueOf(secondLeaderboardData.size()));
        }
    }

    //
    public void setThirdLeaderboard(FirebaseFirestore db, String username){
        this.leaderboard.setValue(null);
        this.userAndTopPlayer.setValue(cache);
        if (!this.fetchThirdLeaderboard) {
            db.collection("Player").orderBy("hasScanned", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean found = false;
                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ScannedNumberRank temp = new ScannedNumberRank(document.getString("username"), document.get("hasScanned", Integer.class));
                            if (Objects.equals(temp.getIdentifier(), username) & !found) {
                                thirdUserAndTopPlayersData.set(3, temp);
                                found = true;
                            }
                            if (index < 3) {
                                thirdUserAndTopPlayersData.set(index, temp);
                                index++;
                            }
                            else{
                                thirdLeaderboardData.add(temp);
                            }
                        }
                        Log.d("Leaderboard 311", String.valueOf(thirdLeaderboardData.size()));
                        leaderboard.setValue(thirdLeaderboardData);
                        userAndTopPlayer.setValue(thirdUserAndTopPlayersData);
                        fetchThirdLeaderboard = true;
                        Log.d("Leaderboard 312", String.valueOf(thirdLeaderboardData.size()));
                    }
                }
            });
        }
        else{
            Log.d("Leaderboard 321", String.valueOf(thirdLeaderboardData.size()));
            this.leaderboard.setValue(this.thirdLeaderboardData);
            this.userAndTopPlayer.setValue(this.thirdUserAndTopPlayersData);
            Log.d("Leaderboard 322", String.valueOf(thirdLeaderboardData.size()));
        }
    }

    //
    public void setLastLeaderboard(FirebaseFirestore db, String username){
        this.leaderboard.setValue(null);
        this.userAndTopPlayer.setValue(cache);
        if (!this.fetchLastLeaderboard) {
            db.collection("Player").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean found = false;
                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TotalScoreRank temp = new TotalScoreRank(document.getString("username"), document.get("score", Integer.class));
                            if (Objects.equals(temp.getIdentifier(), username) & !found) {
                                lastUserAndTopPlayersData.set(3, temp);
                                found = true;
                            }
                            if (index < 3) {
                                lastUserAndTopPlayersData.set(index, temp);
                                index++;
                            }
                            else{
                                lastLeaderboardData.add(temp);
                            }
                        }
                        Log.d("Leaderboard 411", String.valueOf(lastLeaderboardData.size()));
                        leaderboard.setValue(lastLeaderboardData);
                        userAndTopPlayer.setValue(lastUserAndTopPlayersData);
                        fetchLastLeaderboard = true;
                        Log.d("Leaderboard 412", String.valueOf(lastLeaderboardData.size()));
                    }
                }
            });
        }
        else{
            Log.d("Leaderboard 421", String.valueOf(lastLeaderboardData.size()));
            leaderboard.setValue(lastLeaderboardData);
            userAndTopPlayer.setValue(lastUserAndTopPlayersData);
            Log.d("Leaderboard 422", String.valueOf(lastLeaderboardData.size()));
        }
    }

    //
    public void setLeaderboardPosition(int position){
        this.position = position;
        this.leaderboardPosition.setValue(this.position);
    }

    // Refresh data
    public void refreshHistory(){
        this.firstLeaderboardData.clear();
        this.secondLeaderboardData.clear();
        this.thirdLeaderboardData.clear();
        this.lastLeaderboardData.clear();
        Rank rank = new Rank();
        for (int index = 0; index < 4; index++) {
            this.firstUserAndTopPlayersData.set(index, rank);
            this.secondUserAndTopPlayersData.set(index, rank);
            this.thirdUserAndTopPlayersData.set(index, rank);
            this.lastUserAndTopPlayersData.set(index, rank);
        }
        this.position = 0;
        HighestScoreRank first = new HighestScoreRank();
        first.resetThreshold();
        RegionalHighestScoreRank second = new RegionalHighestScoreRank();
        second.resetThreshold();
        ScannedNumberRank third = new ScannedNumberRank();
        third.resetThreshold();
        TotalScoreRank last = new TotalScoreRank();
        last.resetThreshold();
        this.leaderboard.setValue(null);
        this.userAndTopPlayer.setValue(this.firstUserAndTopPlayersData);
        this.leaderboardPosition.setValue(0);
        this.fetchFirstLeaderboard = false;
        this.fetchSecondLeaderboard = false;
        this.fetchThirdLeaderboard = false;
        this.fetchLastLeaderboard = false;
    }

}
