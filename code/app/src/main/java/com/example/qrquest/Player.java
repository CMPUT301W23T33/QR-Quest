package com.example.qrquest;

/**
 * This class defines new players (end-users)
 * @author Dang Viet Anh Dinh
 */
public class Player {

    /**
     * This member represents the email address (contact information) of the player
     */
    private String emailAddress;

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
     * This method defines a default new player profile
     */
    public Player(){}

    /**
     * This method defines a new player profile with only the username
     * @param username: a unique string represented player's username
     */
    public Player(String username) {
        this.username = username;
        this.hasScanned = 0;
        this.highestScore = 0;
        this.score = 0;
    }
    /**
     * This method defines a new player profile with basic attributes
     * @param hasScanned: the number of QR Code scanned by the player
     * @param highestScore: the highest QR Code score of the player
     * @param region: the region of the player
     * @param score: the total QR Code score of the player
     * @param emailAddress: the email address of the player
     * @param username: the username of the player
     */
    public Player(int hasScanned, double highestScore, String region, double score, String emailAddress, String username){
        this.hasScanned = hasScanned;
        this.highestScore = highestScore;
        this.region = region;
        this.score = score;
        this.emailAddress = emailAddress;
        this.username = username;

    }

    /**
     * This method retrieves the email address of the player
     * @return the email address of the player
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * This method sets the email address for the player
     * @param emailAddress: the email address of the player
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

}

