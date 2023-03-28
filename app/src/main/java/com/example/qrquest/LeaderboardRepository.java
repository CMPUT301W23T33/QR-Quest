package com.example.qrquest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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

    // Leaderboard general setup
    private static LeaderboardRepository leaderboardRepository;
    private final MutableLiveData<ArrayList<Rank>> leaderboard = new MutableLiveData<>();
    private Integer position = 0;
    private boolean fetchFirstLeaderboard, fetchSecondLeaderboard, fetchThirdLeaderboard, fetchLastLeaderboard;
    private final MutableLiveData<Integer> leaderboardPosition = new MutableLiveData<>();
    private final MutableLiveData<Rank> user = new MutableLiveData<>();
    private final MutableLiveData<Rank> first = new MutableLiveData<>();
    private final MutableLiveData<Rank> second = new MutableLiveData<>();
    private final MutableLiveData<Rank> third = new MutableLiveData<>();

    // First leaderboard data
    private final ArrayList<Rank> firstLeaderboardData = new ArrayList<>();
    private Rank firstUserData = new Rank();
    private Rank first1stPlayerData = new Rank();
    private Rank first2ndPlayerData = new Rank();
    private Rank first3rdPlayerData = new Rank();

    // Second leaderboard data
    private final ArrayList<Rank> secondLeaderboardData = new ArrayList<>();
    private Rank secondUserData = new Rank();
    private Rank second1stPlayerData = new Rank();
    private Rank second2ndPlayerData = new Rank();
    private Rank second3rdPlayerData = new Rank();

    // Third leaderboard data
    private final ArrayList<Rank> thirdLeaderboardData = new ArrayList<>();
    private Rank thirdUserData = new Rank();
    private Rank third1stPlayerData = new Rank();
    private Rank third2ndPlayerData = new Rank();
    private Rank third3rdPlayerData = new Rank();

    // Last leaderboard data
    private final ArrayList<Rank> lastLeaderboardData = new ArrayList<>();
    private Rank lastUserData = new Rank();
    private Rank last1stPlayerData = new Rank();
    private Rank last2ndPlayerData = new Rank();
    private Rank last3rdPlayerData = new Rank();

    // Get a representative instance of the class
    public static LeaderboardRepository getInstance(){
        if (leaderboardRepository == null){
            leaderboardRepository = new LeaderboardRepository();
        }
        return leaderboardRepository;
    }

    // Get leaderboard
    public MutableLiveData<ArrayList<Rank>> getLeaderboard(){return this.leaderboard;}

    // Get user
    public MutableLiveData<Rank> getUser(){return this.user;}

    // Get 1st player
    public MutableLiveData<Rank> getFirst(){return this.first;}

    // Get 2nd player
    public MutableLiveData<Rank> getSecond(){return this.second;}

    // Get 3rd player
    public MutableLiveData<Rank> getThird(){return this.third;}

    // Get type of leaderboard
    public MutableLiveData<Integer> getLeaderboardPosition(){return this.leaderboardPosition;}

    // Set the first leaderboard for display
    public void setFirstLeaderboard(FirebaseFirestore db, String username){
        this.leaderboard.setValue(null);
        if (!this.fetchFirstLeaderboard) {
            db.collection("main").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean found = false;
                    int index = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HighestScoreRank temp = new HighestScoreRank(document.getString("username"), document.get("score", Integer.class));
                        if (Objects.equals(temp.getIdentifier(), username) && !found) {
                            firstUserData = temp;
                            found = true;
                        }
                        if (index == 0){
                            first1stPlayerData = temp;
                        }
                        else if (index == 1){
                            first2ndPlayerData = temp;
                        }
                        else if (index == 2){
                            first3rdPlayerData = temp;
                        }
                        else{
                            firstLeaderboardData.add(temp);
                        }
                        index++;
                    }
                    setData(firstLeaderboardData, firstUserData, first1stPlayerData, first2ndPlayerData, first3rdPlayerData);
                    fetchFirstLeaderboard = true;
                }
            });
        }
        else{
            setData(firstLeaderboardData, firstUserData, first1stPlayerData, first2ndPlayerData, first3rdPlayerData);
        }
    }

    // Set the second leaderboard for display
    public void setSecondLeaderboard(FirebaseFirestore db, String username, String region){
        this.leaderboard.setValue(null);
        if (!this.fetchSecondLeaderboard) {
            db.collection("main").whereEqualTo("region", region).orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean found = false;
                    int index = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        RegionalHighestScoreRank temp = new RegionalHighestScoreRank(document.getString("username"), document.get("score", Integer.class));
                        if (Objects.equals(temp.getIdentifier(), username) && !found) {
                            secondUserData = temp;
                            found = true;
                        }
                        if (index == 0){
                            second1stPlayerData = temp;
                        }
                        else if (index == 1){
                            second2ndPlayerData = temp;
                        }
                        else if (index == 2){
                            second3rdPlayerData = temp;
                        }
                        else{
                            secondLeaderboardData.add(temp);
                        }
                        index++;
                    }
                    setData(secondLeaderboardData, secondUserData, second1stPlayerData, second2ndPlayerData, second3rdPlayerData);
                    fetchSecondLeaderboard = true;
                }
            });
        }
        else{
            setData(secondLeaderboardData, secondUserData, second1stPlayerData, second2ndPlayerData, second3rdPlayerData);
        }
    }

    // Set the third leaderboard for display
    public void setThirdLeaderboard(FirebaseFirestore db, String username){
        this.leaderboard.setValue(null);
        if (!this.fetchThirdLeaderboard) {
            db.collection("Player").orderBy("hasScanned", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean found = false;
                    int index = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ScannedNumberRank temp = new ScannedNumberRank(document.getString("username"), document.get("hasScanned", Integer.class));
                        if (Objects.equals(temp.getIdentifier(), username) && !found) {
                            thirdUserData = temp;
                            found = true;
                        }
                        if (index == 0){
                            third1stPlayerData = temp;
                        }
                        else if (index == 1){
                            third2ndPlayerData = temp;
                        }
                        else if (index == 2){
                            third3rdPlayerData = temp;
                        }
                        else{
                            thirdLeaderboardData.add(temp);
                        }
                        index++;
                    }
                    setData(thirdLeaderboardData, thirdUserData, third1stPlayerData, third2ndPlayerData, third3rdPlayerData);
                    fetchThirdLeaderboard = true;
                }
            });
        }
        else{
            setData(thirdLeaderboardData, thirdUserData, third1stPlayerData, third2ndPlayerData, third3rdPlayerData);
        }
    }

    // Set the last leaderboard for display
    public void setLastLeaderboard(FirebaseFirestore db, String username){
        this.leaderboard.setValue(null);
        if (!this.fetchLastLeaderboard) {
            db.collection("Player").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean found = false;
                    int index = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TotalScoreRank temp = new TotalScoreRank(document.getString("username"), document.get("score", Integer.class));
                        if (Objects.equals(temp.getIdentifier(), username) && !found) {
                            lastUserData = temp;
                            found = true;
                        }
                        if (index == 0){
                            last1stPlayerData = temp;
                        }
                        else if (index == 1){
                            last2ndPlayerData = temp;
                        }
                        else if (index == 2){
                            last3rdPlayerData = temp;
                        }
                        else{
                            lastLeaderboardData.add(temp);
                        }
                        index++;
                    }
                    setData(lastLeaderboardData, lastUserData, last1stPlayerData, last2ndPlayerData, last3rdPlayerData);
                    fetchLastLeaderboard = true;
                }
            });
        }
        else{
            setData(lastLeaderboardData, lastUserData, last1stPlayerData, last2ndPlayerData, last3rdPlayerData);
        }
    }

    // Set the type of leaderboard
    public void setLeaderboardPosition(int position){
        this.position = position;
        this.leaderboardPosition.setValue(this.position);
    }

    // Refresh rank thresholds
    public void refreshHistory(){
        HighestScoreRank first = new HighestScoreRank();
        first.resetThreshold();
        RegionalHighestScoreRank second = new RegionalHighestScoreRank();
        second.resetThreshold();
        ScannedNumberRank third = new ScannedNumberRank();
        third.resetThreshold();
        TotalScoreRank last = new TotalScoreRank();
        last.resetThreshold();
    }

    // Set data for display
    private void setData(ArrayList<Rank> leaderboardData, Rank userData, Rank top1Data, Rank top2Data, Rank top3Data){
        this.leaderboard.setValue(leaderboardData);
        this.user.setValue(userData);
        this.first.setValue(top1Data);
        this.second.setValue(top2Data);
        this.third.setValue(top3Data);
    }

}