package com.example.qrquest;

/**
 * This class stores the item information in the leaderboard for the highest QR Code score and the regional highest QR Code score
 * @author: Dang Viet Anh Dinh
 */
public class HighestScoreRank extends Rank implements ReusableRank {

    /**
     * This member represents the rank of the queried value in the leaderboard
     */
    private static int queryRank = 0;

    /**
     * This member represents the threshold value to identify duplicate(s)
     */
    private static double thresholdValue = 0;

    /**
     * This member represents the current rank
     */
    private static int rankCursor = 1;

    /**
     * This member represents the cached rank in the leaderboard in case of duplicate(s)
     */
    private static int cache = 1;

    /**
     * This method defines the default leaderboard item
     */
    public HighestScoreRank(){}

    /**
     * This method defines the leaderboard item with its attributes, sets its rank and queried rank when matched
     * @param identifier: the identifier of the item (username, ...)
     * @param value: the item value
     * @param queryValue: the queried value
     */
    public HighestScoreRank(String identifier, double value, double queryValue) {
        super(identifier, value);
        setupThreshold();
        if (value == getScoreThreshold()){
            rankCursorIdled();
        }
        else{
            rankCursorIncremented();
        }
        if (queryValue == getValue()){
            setQueryRank();
        }
    }

    /**
     * This method retrieves the rank of the queried value
     * @param score: the queried value
     * @return: the rank of the queried value
     */
    @Override
    public int getQueryRank(double score) {
        return queryRank;
    }

    /**
     * This method resets any existing thresholds of the leaderboard
     */
    public void resetThreshold(){
        resetQueryRank();
        resetCache(1);
        resetRankCursor(1);
        resetThresholdValue();
    }

    /**
     * This method reset the query rank back to 0
     */
    private void resetQueryRank(){
        queryRank = 0;
    }

    /**
     * This methods sets the query rank to the current item rank
     */
    private void setQueryRank(){
        queryRank = getRank();
    }

    /**
     * This method reset the threshold value of the leaderboard
     */
    private void resetThresholdValue(){
        thresholdValue = 0;
    }

    /**
     * This method retrieves the current threshold value
     * @return: the current threshold value
     */
    private double getScoreThreshold() {
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
     * @param newCache
     */
    private void resetCache(int newCache){
        cache = newCache;
    }

    /**
     * This method sets up the thresholds for the leaderboard
     */
    private void setupThreshold(){
        if (getScoreThreshold() == 0){
            setThresholdValue();
        }
    }

    /**
     * This method updates the leaderboard when the rank cursor is incremented (no duplicates are found for the current item value)
     */
    private void rankCursorIncremented(){
        setRank(getCache());
        resetRankCursor(getCache() + 1);
        resetCache(getRankCursor());
        setThresholdValue();
    }

    /**
     * This method updates the leaderboard when the rank cursor is idled (duplicate(s) are found for the current item value)
     */
    private void rankCursorIdled(){
        setRank(getRankCursor());
        resetCache(getCache() + 1);
    }

}
