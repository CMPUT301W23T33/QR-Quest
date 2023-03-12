package com.example.qrquest;

import java.io.Serializable;

/**
 * This class defines QR Codes
 */
public class QRCode {

    /**
     * This member represents the hashed name of the QR Code
     */
    private String hashedQRCode;

    /**
     * This member represents the un-hashed name of the QR Code
     */
    private String qrCode;

    /**
     * This member represents the value of the longitude of the QR Code
     */
    private double score;

    /**
     * This member represents the value of the latitude of the QR Code
     */
    private double latitude;

    /**
     * This member represents the value of the longitude of the QR Code
     */
    private double longitude;

    /**
     * This method defines the default QR Code
     */
    public QRCode(){}

    /**
     * This method defines an existing QR Code in the database
     * @param hashedQRCode: the hashed name of the QR Code
     * @param qrCode: the un-hashed name of the QR Code
     * @param score: the score of the QR Code
     * @param latitude: the latitude of the QR Code
     * @param longitude: the longitude of the QR Code
     */
    public QRCode(String hashedQRCode, String qrCode, double score, double latitude, double longitude){
        setHashedQRCode(hashedQRCode);
        setQrCode(qrCode);
        setScore(score);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * This method defines a new QR Code
     * @param qrCode: the un-hashed name of the QR Code
     * @param score: the score of the QR Code
     * @param latitude: the latitude of the QR Code
     * @param longitude: the longitude of the QR Code
     */
    public QRCode(String qrCode, double score, double latitude, double longitude){
        hashQRCode();
        setQrCode(qrCode);
        setScore(score);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * This method retrieves the un-hashed name of the QR Code
     * @return the un-hashed name of the QR Code
     */
    public String getQrCode() {
        return this.qrCode;
    }

    /**
     * This method set the un-hashed name for the QR Code and performs QR Code hashing
     * @param qrCode: the un-hashed name of the QR Code
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * This method retrieves the hashed name of the QR Code
     * @return the hashed name of the QR Code
     */
    public String getHashedQRCode() {
        return this.hashedQRCode;
    }

    /**
     * This method sets the hashed name (if provided) for the QR Code
     * @param hashedQRCode: the hashed name of the QR Code
     */
    public void setHashedQRCode(String hashedQRCode){
        this.hashedQRCode = hashedQRCode;
    }

    /**
     * This method hashes the raw name of the QR Code
     */
    public void hashQRCode(){

    }

    /**
     * This method retrieves the score of the QR Code
     * @return the score of the QR Code
     */
    public double getScore() {
        return this.score;
    }

    /**
     * This method sets the score for the QR Code
     * @param score: the score of the QR Code
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * This method retrieves the latitude of the QR Code
     * @return the latitude of the QR Code
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * This method sets the latitude for the QR Code
     * @param latitude: the latitude of the QR Code
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * This method retrieves the longitude of the QR Code
     * @return the longitude of the QR Code
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * This method sets the longitude for the QR Code
     * @param longitude: the longitude of a geolocation
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}

