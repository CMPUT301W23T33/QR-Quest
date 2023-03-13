package com.example.qrquest;

/**
 * This class defines the ranking of players
 * Call ArrayList<> of classes that inherit this class to use -> one of HighestScoreRank, RegionalHighestScoreRank, QRCodeNumberRank, TotalScoreRank
 * E.g: ArrayList<TotalScoreRank>
 * @author Dang Viet Anh Dinh
 */
public class Rank {

    /**
     * This member represents the identifier of the item (player's username or QR Code name)
     */
    private String identifier;

    /**
     * This member represents the rank of the item
     */
    private int rank;

    /**
     * This member represents the value of the item
     */
    private int value;

    /**
     * This method defines the default rank of an item
     */
    public Rank(){}

    public Rank(String identifier, int value){
        setIdentifier(identifier);
        setValue(value);
    }

    /**
     * This method retrieves the identifier of the item
     * @return a string that identifies the item
     */
    public String getIdentifier(){
        return this.identifier;
    }

    /**
     * This method retrieves the rank of the item
     * @return the rank of the item
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * This method retrieves the value of the item
     * @return the value of the item
     */
    public int getValue() {
        return this.value;
    }

    /**
     * This methods sets the identifier for the item
     * @param identifier: the identifier of the item
     */
    private void setIdentifier(String identifier){
        this.identifier = identifier;
    }

    /**
     * This method sets the rank for the item
     * @param rank: the rank of the item
     */
    protected void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * This method sets the value for the item
     * @param value: the value of the item
     */
    private void setValue(int value) {
        this.value = value;
    }

}
