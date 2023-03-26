package com.example.qrquest;

import java.util.ArrayList;

/**
 * This class stores the item information in the leaderboard for the regional highest QR Code score
 * @author Dang Viet Anh Dinh
 */
public class RegionalHighestScoreRank extends Rank{

    /**
     * This member represents the threshold value to identify duplicate(s)
     */
    private static int thresholdValue = 0;

    /**
     * This member represents the current rank
     */
    private static int rankCursor = 0;

    /**
     * This member represents the cached rank in the leaderboard in case of duplicate(s)
     */
    private static int cache = 0;

    /**
     * This member represents the distinct score in the leaderboard
     */
    private static ArrayList<Integer> scoreLeaderboard;

    /**
     * This member represents the rank of the distinct score in the leaderboard
     */
    private static ArrayList<Integer> rankLeaderboard;

    /**
     * This method defines the default leaderboard item
     */
    public RegionalHighestScoreRank(){}

    /**
     * This method defines the leaderboard item with its attributes, sets its rank and queried rank when matched
     * @param identifier: the identifier of the item (username, ...)
     * @param value: the item value
     */
    public RegionalHighestScoreRank(String identifier, int value){
        super(identifier, value);
        setupLeaderboard();
        if (value == getScoreThreshold()){
            rankCursorIdled();
        }
        else{
            rankCursorIncremented();
        }
    }

    /**
     * This method retrieves the rank of the queried value
     * @param score: the queried value
     * @return the rank of the queried value or 0 if not found
     */
    public static int getQueryRank(int score) {
        int index = scoreLeaderboard.indexOf(score);
        if (index == -1){
            return 0;
        }
        else{
            return rankLeaderboard.get(index);
        }
    }

    /**
     * This method resets any existing thresholds of the leaderboard
     */
    public void resetThreshold(){
        resetCache(0);
        resetRankCursor(0);
        resetThresholdValue();
    }

    /**
     * This method reset the threshold value of the leaderboard
     */
    private void resetThresholdValue(){
        thresholdValue = 0;
    }

    /**
     * This method retrieves the current threshold value
     * @return the current threshold value
     */
    private int getScoreThreshold() {
        return thresholdValue;
    }

    /**
     * This method sets the threshold value to be the current item value of the leaderboard
     */
    private void setThresholdValue(){
        thresholdValue = getValue();
    }

    /**
     * This method retrieves the rank cursor
     * @return the rank cursor
     */
    private int getRankCursor() {
        return rankCursor;
    }

    /**
     * This method (re)sets the rank cursor with a new value corresponding to the current item of the leaderboard
     * @param newRankCursor: the new rank cursor
     */
    private void resetRankCursor(int newRankCursor){
        rankCursor = newRankCursor;
    }

    /**
     * This method retrieves the cache of the leaderboard
     * @return the cache of the leaderboard
     */
    private int getCache() {
        return cache;
    }

    /**
     * This method (re)sets the cache with a new value corresponding to the current item of the leaderboard
     * @param newCache: the cache of the current item
     */
    private void resetCache(int newCache){
        cache = newCache;
    }

    /**
     * This method sets up the leaderboard
     */
    private void setupLeaderboard(){
        if (scoreLeaderboard == null){
            scoreLeaderboard = new ArrayList<>();
            rankLeaderboard = new ArrayList<>();
        }
    }

    /**
     * This method updates the leaderboard when the rank cursor is incremented (no duplicates are found for the current item value)
     */
    private void rankCursorIncremented(){
        resetRankCursor(getCache() + 1);
        resetCache(getRankCursor());
        setThresholdValue();
        setRank(getCache());
        scoreLeaderboard.add(getValue());
        rankLeaderboard.add(getRank());
    }

    /**
     * This method updates the leaderboard when the rank cursor is idled (duplicate(s) are found for the current item value)
     */
    private void rankCursorIdled(){
        setRank(getRankCursor());
        resetCache(getCache() + 1);
    }

}