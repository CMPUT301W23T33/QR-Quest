package com.example.qrquest;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player {

    // Player's attributes
    private String name;
    private String emailAddress;
    private ArrayList<QRCode> history;
    private double score = 0;
    private int scanned = 0;
    private String region;


    // Player's modifiers
    public Player(){}

    // Defining a new player profile
    public Player(String emailAddress){
        setEmailAddress(emailAddress);
        // Player name can be randomized or ...
        setHistory(new ArrayList<QRCode>());
        setScore(0);
        setScanned(0);
    }

    // Loading an existing player profile
    public Player(String name, String emailAddress, ArrayList<QRCode> history, double score, int scanned) {
        setName(name);
        setEmailAddress(emailAddress);
        setHistory(history);
        setScore(score);
        setScanned(scanned);
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

    public double getScore() {
        return score;
    }

    public int getScanned() {
        return scanned;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(double score) {
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

