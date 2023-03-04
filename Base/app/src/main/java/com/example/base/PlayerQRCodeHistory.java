package com.example.base;

/**
 * This class defines users' past, compact QR Codes
 * Call ArrayList<UserQRCodeHistory> to use this class
 * @author Dang Viet Anh Dinh
 */
public class PlayerQRCodeHistory {

    /**
     * This member represents the hashed name of the QR Code
     */
    private String qrCode;

    /**
     * This member represents the value of the score of the QR Code
     */
    private double score;

    /**
     * This method defines a default compact QR Code
     */
    public PlayerQRCodeHistory(){}

    /**
     * This method defines the compact QR Code scanned by a player
     * @param qrCode: the QR Code name
     * @param score: the QR Code score
     */
    public PlayerQRCodeHistory(String qrCode, double score){
        this.qrCode = qrCode;
        this.score = score;
    }

    /**
     * This method retrieves the hashed QR Code name
     * @return te hashed QR Code name
     */
    public String getQrCode() {
        return this.qrCode;
    }

    /**
     * This method retrieves the score of the QR Code
     * @return the score of the QR Code
     */
    public double getScore(){
        return this.score;
    }

}
