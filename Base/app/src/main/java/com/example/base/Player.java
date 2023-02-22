package com.example.base;

import java.io.Serializable;
import java.util.LinkedList;

public class Player{

    // Player's attributes
    private String name;
    private String emailAddress;
    private LinkedList<QRCode> history;
    private double score = 0;
    private int scanned = 0;
    // Region can be set when creating/fetching player profile
    // In progress...
    private int region;


    // Player's modifiers
    public Player(){}

    // Defining a new player profile
    public Player(String emailAddress){
        setEmailAddress(emailAddress);
        // Player name can be randomized or ...
        setHistory(new LinkedList<QRCode>());
        setScore(0);
        setScanned(0);
    }

    // Loading an existing player profile
    public Player(String name, String emailAddress, LinkedList<QRCode> history, double score, int scanned) {
        setName(name);
        setEmailAddress(emailAddress);
        setHistory(history);
        setScore(score);
        setScanned(scanned);
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
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

    public LinkedList<QRCode> getHistory() {
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

    public void setHistory(LinkedList<QRCode> history) {
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
