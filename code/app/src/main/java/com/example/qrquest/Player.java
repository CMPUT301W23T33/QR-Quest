package com.example.qrquest;

import java.util.ArrayList;

public class Player {

    // Player's attributes
    private String username;
    private String region;
    private String email;
    private float score = 0;
    private float highestScore = 0;
    private int hasScanned = 0;


    // Constructors
    public Player(){}

    // Defining a new player profile (Missing username, region)
    public Player(String username){
        this.username = username;
        this.score = 0;
        this.hasScanned = 0;
        this.highestScore = 0;
    }

    // Loading an existing player profile
    public Player(String username, float score, float highestScore,
                  int hasScanned, String region, String email) {
        this.username = username;
        this.score = score;
        this.highestScore = highestScore;
        this.hasScanned = hasScanned;
        this.region = region;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

