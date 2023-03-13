package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;


import org.junit.Test;

public class PlayerTest {
    @Test
    /**
     * This function tests the getter and setter methods related to a player's email address
     */
    public void testEmailAddress() {
        Player testPlayer = new Player();
        assertEquals(null,testPlayer.getEmailAddress());
        testPlayer.setEmailAddress("test@gmail.com");
        assertSame("test@gmail.com",testPlayer.getEmailAddress());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a player's username
     */
    public void testUsername() {
        Player testPlayer = new Player();
        assertEquals(null,testPlayer.getEmailAddress());
        testPlayer.setEmailAddress("uniqueuser1");
        assertNotEquals("uniqueuser2",testPlayer.getEmailAddress());
        assertEquals("uniqueuser1",testPlayer.getEmailAddress());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's region
     */
    public void testPlayerRegion() {
        Player testPlayer = new Player();
        assertEquals(null,testPlayer.getRegion());
        testPlayer.setRegion("Canada");
        assertNotEquals("Brazil",testPlayer.getRegion());
        assertSame("Canada",testPlayer.getRegion());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's number of scanned QRCodes
     */
    public void testScannedAmount() {
        Player testPlayer = new Player();
        assertEquals(0,testPlayer.getHasScanned());
        testPlayer.setHasScanned(5);
        assertNotEquals(2,testPlayer.getHasScanned());
        assertSame(5,testPlayer.getHasScanned());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's highest score
     */
    public void testHighestScore() {
        Player testPlayer = new Player();
        assertEquals(0.0,testPlayer.getHighestScore(),0);
        testPlayer.setHighestScore(100.0);
        assertNotEquals(99.0,testPlayer.getHighestScore());
        assertSame(100.0,testPlayer.getHighestScore());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's total score
     */
    public void testTotalScore() {
        Player testPlayer = new Player();
        assertEquals(0.0,testPlayer.getScore(),0.0);
        testPlayer.setScore(1234);
        System.out.println(testPlayer.getScore());
        assertNotEquals( 1233.0,testPlayer.getScore());
        assertSame(Double.valueOf(1234),Double.valueOf(testPlayer.getScore()));
    }
}
