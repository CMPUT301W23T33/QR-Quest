package com.example.base;

/**
 * This class defines players (end-users)
 * @author Dang Viet Anh Dinh
 */
public class ExpandedPlayer {

    /**
     * This member represents the unique identifier of the player
     */
    private String uniqueIdentifier;

    /**
     * This member represents the username of the player
     */
    private String username;

    /**
     * This member represents the region of the player
     */
    private String region;

    /**
     * This member represents the number of QR Codes the player has scanned
     */
    private int hasScanned;

    /**
     * This member represents the highest QR Codes score of the player
     */
    private double highestScore;

    /**
     * This member represents the total score of the player
     */
    private double score;

    /**
     * This member represents the highest scoring QR Code rank of the player
     */
    private int scoringQRCodeRank;

    /**
     * This member represents the number of QR Codes rank of the player
     */
    private int hasScannedRank;

    /**
     * This member represents the total QR Code score rank of the player
     */
    private int scoreRank;

    /**
     * This member represents the regional highest scoring QR Code rank of the player
     */
    private int regionalScoringQRCodeRank;

    /**
     * This method defines a default compact QR Code
     */
    public ExpandedPlayer(){}

    /**
     * This method retrieves the unique identifier of the player
     * @return the unique identifier of the player
     */
    public String getUniqueIdentifier() {
        return this.uniqueIdentifier;
    }

    /**
     * This method sets the unique identifier for the player
     * @param uniqueIdentifier: the unique identifier of the player
     */
    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    /**
     * This method retrieves the username of the player
     * @return the username of the player
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * This method sets the username for the player
     * @param username: the username of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method retrieves the region of the player
     * @return the region of the player
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * This method sets the region for the player
     * @param region: the region of the player
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * This method retrieves the number of scanned QR Codes by the player
     * @return the number of scanned QR Codes by the player
     */
    public int getHasScanned() {
        return this.hasScanned;
    }

    /**
     * This method sets the number of scanned QR Codes by the player
     * @param hasScanned: the number of scanned QR Codes by the player
     */
    public void setHasScanned(int hasScanned) {
        this.hasScanned = hasScanned;
    }

    /**
     * This method retrieves the highest QR Code score of the player
     * @return the highest QR Code score of the player
     */
    public double getHighestScore() {
        return this.highestScore;
    }

    /**
     * This method sets the highest QR Code score for the player
     * @param highestScore: the highest QR Code score for the player
     */
    public void setHighestScore(double highestScore) {
        this.highestScore = highestScore;
    }

    /**
     * This method retrieves the total score of the player
     * @return the total score of the player
     */
    public double getScore() {
        return this.score;
    }

    /**
     * This method sets the total score for the player
     * @param score: the total score of the player
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * This method retrieves the highest scoring QR Code rank of the player
     * @return the highest scoring QR Code rank of the player
     */
    public int getScoringQRCodeRank() {
        return this.scoringQRCodeRank;
    }

    /**
     * This method sets the highest scoring QR Code rank for the player
     * @param scoringQRCodeRank: the highest scoring QR Code rank of the player
     */
    public void setScoringQRCodeRank(int scoringQRCodeRank) {
        this.scoringQRCodeRank = scoringQRCodeRank;
    }

    /**
     * This method retrieves the number of scanned QR Codes rank of the player
     * @return
     */
    public int getHasScannedRank() {
        return this.hasScannedRank;
    }

    /**
     * This method sets the number of scanned QR Codes rank for the player
     * @param hasScannedRank: the number of scanned QR Codes rank of the player
     */
    public void setHasScannedRank(int hasScannedRank) {
        this.hasScannedRank = hasScannedRank;
    }

    /**
     * This method retrieves the total QR Code score of the player
     * @return the total QR Code score of the player
     */
    public int getScoreRank() {
        return this.scoreRank;
    }

    /**
     * This method sets the total QR Code score for the player
     * @param scoreRank: the total QR Code score of the player
     */
    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    /**
     * This method retrieves the regional highest scoring QR Code rank of the player
     * @return the regional, highest scoring QR Code rank of the player
     */
    public int getRegionalScoringQRCodeRank() {
        return this.regionalScoringQRCodeRank;
    }

    /**
     * This method sets the regional highest scoring QR Code rank for the player
     * @param regionalScoringQRCodeRank: the regional highest scoring QR Code rank of the player
     */
    public void setRegionalScoringQRCodeRank(int regionalScoringQRCodeRank) {
        this.regionalScoringQRCodeRank = regionalScoringQRCodeRank;
    }

}
