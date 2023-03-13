package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * This test class is for testing the TotalScoreRank class, as well as the HighestScoreRank and ScannedNumberRank
 * classes, as they all have the same attributes and methods
 * @author Abinand Nanthananthan
 */
public class TotalScoreRankTesting {

    @Test
    /**
     * This function tests the methods related to a queried Player/Value's rank
     */
    public void testQueryRank() {
        TotalScoreRank testTotalScoreRank = new TotalScoreRank();
        assertEquals(0,testTotalScoreRank.getQueryRank(0));
        testTotalScoreRank.setRank(1);
        assertNotEquals(2,testTotalScoreRank.getRank());
        assertSame(1,testTotalScoreRank.getRank());
    }

    @Test
    /**
     * This function tests the methods related to a player's Rank uniqueness
     */
    public void testThreshold() {
        TotalScoreRank testTotalScoreRank = new TotalScoreRank();
        assertEquals(0,testTotalScoreRank.getScoreThreshold());
        testTotalScoreRank.setThresholdValue();
        assertNotEquals(2,testTotalScoreRank.getScoreThreshold());
        assertSame(0,testTotalScoreRank.getScoreThreshold());
        testTotalScoreRank.resetThreshold();
        assertEquals(0,testTotalScoreRank.getScoreThreshold());
    }

    @Test
    /**
     * This function tests the methods related to a player's current Rank
     */
    public void testRankCursor() {
        TotalScoreRank testTotalScoreRank = new TotalScoreRank();
        assertEquals(1,testTotalScoreRank.getRankCursor());
        testTotalScoreRank.resetRankCursor(3);
        assertNotEquals(2,testTotalScoreRank.getRankCursor());
        assertSame(3,testTotalScoreRank.getRankCursor());
    }

    @Test
    /**
     * This function tests the methods related to a player's cached Rank
     */
    public void testCache() {
        TotalScoreRank testTotalScoreRank = new TotalScoreRank();
        assertEquals(1,testTotalScoreRank.getCache());
        testTotalScoreRank.resetCache(50);
        assertNotEquals(100,testTotalScoreRank.getCache());
        assertSame(50,testTotalScoreRank.getCache());
    }

}
