package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * This test class serves to test the Rank class methods
 * @author Abinand Nanthananthan
 */
public class RankTesting {
    @Test
    /**
     * This function tests the getter and setter methods related to a player's Rank identifier
     */
    public void testIdentifier() {
        Rank testRank = new Rank();
        assertNull(testRank.getIdentifier());
        testRank.setIdentifier("1st");
        assertNotEquals("2nd",testRank.getIdentifier());
        assertSame("1st",testRank.getIdentifier());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's Rank
     */
    public void testRank() {
        Rank testRank = new Rank();
        assertEquals(0,testRank.getRank());
        testRank.setRank(1);
        assertNotEquals(2,testRank.getRank());
        assertSame(1,testRank.getRank());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a player's Rank value
     */
    public void testValue() {
        Rank testRank = new Rank();
        assertEquals(0.0,testRank.getValue(),1);
        testRank.setValue(230);
        assertNotEquals(232,testRank.getValue());
        assertEquals(230,testRank.getValue(),0.1);
    }


}
