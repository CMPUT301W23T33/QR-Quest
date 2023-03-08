package com.example.qrquest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class defines QR Codes in relation with players
 * @author Dang Viet Anh Dinh
 */
public class Info {

    /**
     * This member represents the unhashed name of the QR Code
     */
    private String qrCode;

    /**
     * This member represents the hashed name of the QR Code
     */
    private String hashedQRCode;

    /**
     * This member represents a comment for the QR Code
     */
    private String comment;

    /**
     * This member represents the region of the QR Code when scanned
     */
    private String region;

    /**
     * This member represents the username of the player that scanned the QR Code
     */
    private String username;

    /**
     * This member represents the originally recorded date and time of the QR Code
     */
    private Date timeStamp;

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
     * This method defines a default QR Code
     */
    public Info() {}

    /**
     * This method defines an existing QR Code in the database comprised with its associated player
     * Use this constructor when getting existing Info from
     * @param comment: the comment by the player
     * @param hashedQRCode: the hashed name of the QR Code
     * @param latitude: the latitude of the QR Code
     * @param longitude: the longitude of the QR Code
     * @param qrCode: the unhashed name of the QR Code
     * @param score: the score of the QR Code
     * @param username: the username of the player
     */
    public Info(String comment, String hashedQRCode, double latitude, double longitude, String qrCode, String region, double score, Date timeStamp, String username){
        setComment(comment);
        setHashedQRCode(hashedQRCode);
        setLatitude(latitude);
        setLongitude(longitude);
        setQrCode(qrCode);
        setRegion(region);
        setScore(score);
        setTimeStamp(timeStamp);
        setUsername(username);
    }

    /**
     * This method defines a new QR Code comprised with its associated player
     * @param comment: the comment by the player
     * @param latitude: the latitude of the QR Code
     * @param longitude: the longitude of the QR Code
     * @param qrCode: the unhashed name of the QR Code
     * @param score: the score of the QR Code
     * @param username: the username of the player
     */
    public Info(String comment, double latitude, double longitude, String qrCode, String region, double score, Date timeStamp, String username){
        setComment(comment);
        setLatitude(latitude);
        setLongitude(longitude);
        setQrCode(qrCode);
        setRegion(region);
        setScore(score);
        setTimeStamp(timeStamp);
        setUsername(username);
    }

    /**
     * This method retrieves the unhashed name of the QR Code
     * @return the unhashed name of the QR Code
     */
    public String getQrCode() {
        return this.qrCode;
    }

    /**
     * This method set the unhashed name for the QR Code and performs QR Code hashing
     * @param qrCode: the unhashed name of the QR Code
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
     * This method retrieves the set comment for the QR Code
     * @return the set comment for the QR Code
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * This method sets a comment for the QR Code
     * @param comment: a comment made by a player
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * This method retrieves the region of the QR Code when scanned
     * @return the region of the QR Code when scanned
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * This method sets the region for the QR Code when scanned
     * @param region: the region of the QR Code when scanned
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * This method retrieves the username of the player that scanned the QR Code
     * @return the username of the player that scanned the QR Code
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * This method sets the username of the player for the QR Code
     * @param username: the username of the player that scanned the QR Code
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method retrieves the original date and time when the QR Code was uploaded to the database
     * @return the originally recorded date and time of the QR Code
     */
    public Date getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * This method sets the original date and time when the QR Code was uploaded to the database
     * @param timeStamp: the originally recorded date and time of the QR Code
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
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

    /**
     * This method retrieves the location of the image of the QR Code
     * @return the absolute path to the image of the QR Code in the database
     */
    public String getImagePath(){
        String time = new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss").format(getTimeStamp());
        return "Images/" + getUsername() + "_" + getQrCode() + "_" + time + ".jpg"; // Depending on the requirement this could be hashed/unhashed
        // E.g: getImagePath() -> "username1_QRCode1_03_06_2023_04_20_00.jpg"
    }

    // In the future we may need (some) method(s) for comparing the QR Code

}