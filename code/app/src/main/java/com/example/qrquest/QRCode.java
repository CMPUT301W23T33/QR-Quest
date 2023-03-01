package com.example.qrquest;

public class QRCode {

    // QR Code's attributes
    private double score;
    private String name;
    private double latitude;
    private double longitude;

    // Still working on this one
//    private boolean image;

    // QR Code's modifiers
    public QRCode(){}

    // We can hash QR Code here
    public QRCode(double score, String name, double latitude, double longitude) {
        setScore(score);
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
