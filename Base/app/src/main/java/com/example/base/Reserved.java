package com.example.base;

/**
 * This class defines the ranking of players
 * Call ArrayList<> of classes that inherit this class to
 * Call ArrayList<Rank> to use this class
 * @author Dang Viet Anh Dinh
 */
public class Reserved {
    private int rank;
    private String username;
    private double score;
    private static double queryScore;
    private static int queryRank;
    private static double scoreThreshold = 0;
    private static int rankCursor = 1;
    private static int cache = 1;

    public Reserved(){}

    public Reserved(String username, double score, double queryScore){
        setUsername(username);
        setScore(score);
        setupThreshold(queryScore);
        if (score == getScoreThreshold()){
            rankCursorIdled();
        }
        else{
            rankCursorIncremented();
        }
        if (getScore() == getQueryScore()){
            setQueryRank();
        }
    }

    public int getRank() {
        return this.rank;
    }

    public String getUsername() {
        return this.username;
    }

    public double getScore() {
        return this.score;
    }

    public double getQueryScore(){
        return queryScore;
    }

    public int getQueryRank(){
        return queryRank;
    }

    private void setRank(int rank) {
        this.rank = rank;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setScore(double score) {
        this.score = score;
    }

    private void setQueryScore(double score){
        queryScore = score;
    }

    private void setQueryRank(){
        queryRank = getRank();
    }

    private double getScoreThreshold() {
        return scoreThreshold;
    }

    private void setScoreThreshold(){
        scoreThreshold = getScore();
    }

    private int getRankCursor() {
        return rankCursor;
    }

    private void resetRankCursor(int newRankCursor){
        rankCursor = newRankCursor;
    }

    private int getCache() {
        return cache;
    }

    private void resetCache(int newCache){
        cache = newCache;
    }

    private void setupThreshold(double queryScore){
        if (getScoreThreshold() == 0){
            setScoreThreshold();
            setQueryScore(queryScore);
        }
    }

    private void rankCursorIncremented(){
        setRank(getCache());
        resetRankCursor(getCache() + 1);
        resetCache(getRankCursor());
        setScoreThreshold();
    }

    private void rankCursorIdled(){
        setRank(getRankCursor());
        resetCache(getCache() + 1);
    }

}
