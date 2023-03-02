package com.example.qrquest;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player {

    // Player's attributes
    private String name;
    private String emailAddress;
    private int score = 0;
    private int scanned = 0;
    private String region;
    private ArrayList<QRCode> history;


    // Constructors
    public Player(){}

    // Defining a new player profile (Missing username, region)
    public Player(String name){
        setName(name);
        setEmailAddress("None");
        setScore(0);
        setScanned(0);
    }

    // Loading an existing player profile
    public Player(String name, String emailAddress, int score, int scanned, String region) {
        setName(name);
        setEmailAddress(emailAddress);
        setScore(score);
        setScanned(scanned);
        setRegion(region);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public ArrayList<QRCode> getHistory() {
        return history;
    }

    public int getScore() {
        return score;
    }

    public int getScanned() {
        return scanned;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setScanned(int scanned) {
        this.scanned = scanned;
    }

    public void setHistory(ArrayList<QRCode> history) {
        this.history = history;
    }

    public void addQRCode(QRCode qrCode){
        this.history.add(qrCode);
        this.scanned+= 1;
    }

    public void removeQRCode(int position){
        this.history.remove(position);
        this.scanned-= 1;
    }

}

