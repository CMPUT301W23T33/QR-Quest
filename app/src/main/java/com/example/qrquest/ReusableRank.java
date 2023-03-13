package com.example.qrquest;

/**
 * This interface allows the ArrayList<> of Rank's children classes to be reusable
 * @author Dang Viet Anh Dinh
 */
public interface ReusableRank {

    /**
     * This method resets any existing thresholds
     * Create a method like resetThreshold() in the the Activity/Fragment and call it AFTER a query or BEFORE a new query to erase existing thresholds
     * private void resetThreshold(Rank itemList){
     *         if (itemList instanceof HighestScoreRank) {
     *             HighestScoreRank hsr = (HighestScoreRank) itemList;
     *             hsr.resetThreshold();
     *         }
     *         else if (itemList instanceof TotalScoreRank){
     *             TotalScoreRank tsr = (TotalScoreRank) itemList;
     *             tsr.resetThreshold();
     *         }
     *         else{
     *             QRCodeNumberRank qrcnr = (QRCodeNumberRank) itemList;
     *             qrcnr.resetThreshold();
     *         }
     *     }
     */
    public void resetThreshold();

    /**
     * This method retrieves the rank of a particular value
     * @param value: the item value
     * @return the rank of the item value or 0 if not found
     */
    public int getQueryRank(int value);

}
