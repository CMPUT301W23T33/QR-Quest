package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

public class PlayerTest {
    @Test
    /**
     * This function tests the getter and setter methods related to a player's email address
     */
    public void testEmailAddress() {
        Player testPlayer = new Player();
        assertNull(testPlayer.getEmailAddress());
        testPlayer.setEmailAddress("test@gmail.com");
        assertNotEquals("real@gmail.com",testPlayer.getEmailAddress());
        assertSame("test@gmail.com",testPlayer.getEmailAddress());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a player's username
     */
    public void testUsername() {
        Player testPlayer = new Player();
        assertNull(testPlayer.getEmailAddress());
        testPlayer.setEmailAddress("uniqueuser1");
        assertNotEquals("uniqueuser2",testPlayer.getEmailAddress());
        assertSame("uniqueuser1",testPlayer.getEmailAddress());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's region
     */
    public void testPlayerRegion() {
        Player testPlayer = new Player();
        assertNull(testPlayer.getRegion());
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
        assertEquals(0,testPlayer.getHighestScore());
        testPlayer.setHighestScore(100);
        assertNotEquals(99,testPlayer.getHighestScore());
        assertSame(100,testPlayer.getHighestScore());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a player's total score
     */
    public void testTotalScore() {
        Player testPlayer = new Player();
        assertEquals(0,testPlayer.getScore());
        testPlayer.setScore(1234);
        assertNotEquals( 1233,testPlayer.getScore());
        assertEquals(1234, testPlayer.getScore());
    }
}
