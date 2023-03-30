package com.example.qrquest;

/**
 * This class stores the item information in the leaderboard for the most scanned QR Codes
 * @author Dang Viet Anh Dinh
 */
public class ScannedNumberRank extends Rank{

    /**
     * This member represents the current rank
     */
    private static int rankCursor = 0;

    /**
     * This method defines the default leaderboard item
     */
    public ScannedNumberRank(){}

    /**
     * This method defines the leaderboard item with its attributes, sets its rank and queried rank when matched
     * @param identifier: the identifier of the item (username, ...)
     * @param value: the item value
     */
    public ScannedNumberRank(String identifier, int value){
        super(identifier, value);
        rankCursorIncremented();
    }

    /**
     * This method resets the rank cursor of the leaderboard
     */
    public void resetThreshold(){
        resetRankCursor(0);
    }

    /**
     * This method retrieves the rank cursor
     * @return the rank cursor
     */
    private int getRankCursor() {
        return rankCursor;
    }

    /**
     * This method (re)sets the rank cursor with a new value corresponding to the current item of the leaderboard
     * @param newRankCursor: the new rank cursor
     */
    private void resetRankCursor(int newRankCursor){
        rankCursor = newRankCursor;
    }

    /**
     * This method updates the leaderboard when the rank cursor is incremented (no duplicates are found for the current item value)
     */
    private void rankCursorIncremented(){
        resetRankCursor(getRankCursor() + 1);
        setRank(getRankCursor());
    }
}
