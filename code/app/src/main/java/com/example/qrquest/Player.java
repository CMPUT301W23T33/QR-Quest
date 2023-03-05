package com.example.qrquest;

import java.util.ArrayList;

public class Player {

    // Player's attributes
    private String username;
    private String uniqueIdentifier;
    private String region;
    private float score = 0;
    private float highestScore = 0;
    private int hasScanned = 0;


    // Constructors
    public Player(){}

    // Defining a new player profile (Missing username, region)
    public Player(String username, String uniqueIdentifier){
        setUsername(username);
        setUniqueIdentifier(uniqueIdentifier);
        setScore(0);
        setHighestScore(0);
        setHasScanned(0);
    }

    // Loading an existing player profile
    public Player(String username, String uniqueIdentifier, float score, float highestScore,
                  int hasScanned, String region) {
        setUsername(username);
        setUniqueIdentifier(uniqueIdentifier);
        setScore(score);
        setHighestScore(highestScore);
        setHasScanned(hasScanned);
        setRegion(region);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getHasScanned() {
        return hasScanned;
    }

    public void setHasScanned(int hasScanned) {
        this.hasScanned = hasScanned;
    }


    public float getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(float highestScore) {
        this.highestScore = highestScore;
    }
}

