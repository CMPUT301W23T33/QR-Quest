package com.example.qrquest;

/**
 * This class defines users' past, compact QR Codes
 * @author Dang Viet Anh Dinh
 */
public class History {

    /**
     * This member represents the hashed name of the QR Code
     */
    private String qrCode;

    /**
     * This member represents the value of the score of the QR Code
     */
    private int score;

    /**
     * This method defines a default compact QR Code
     */
    public History(){}

    /**
     * This method defines the compact QR Code scanned by a player
     * @param qrCode the QR Code name
     * @param score the QR Code score
     */
    public History(String qrCode, int score){
        setQrCode(qrCode);
        setScore(score);
    }

    /**
     * This method retrieves the hashed QR Code name
     * @return the hashed QR Code name
     */
    public String getQrCode() {
        return this.qrCode;
    }

    /**
     * This method retrieves the score of the QR Code
     * @return the score of the QR Code
     */
    public int getScore(){
        return this.score;
    }

    /**
     * This method sets the hashed name for the QR Code
     * @param qrCode the hashed QR Code name
     */
    private void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * This method sets the score for the QR Code
     * @param score the score of the QR Code
     */
    private void setScore(int score) {
        this.score = score;
    }

}
