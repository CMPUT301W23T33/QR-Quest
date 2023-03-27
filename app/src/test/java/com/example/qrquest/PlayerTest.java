package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests class Player
 * @author Thea Nguyen
 */
public class PlayerTest {
    Player player = new Player();

    @Test
    public void getSetEmailAddressTest() {
        String emailAddress = "qrquest2506@gmail.com";
        assertNull(player.getEmailAddress());
        player.setEmailAddress(emailAddress);
        assertNotNull(player.getEmailAddress());
        assertEquals(player.getEmailAddress(), emailAddress);
    }

    @Test
    public void getSetUsernameTest() {
        String username = "Dogwood";
        assertNull(player.getUsername());
        player.setUsername(username);
        assertNotNull(player.getUsername());
        assertEquals(player.getUsername(), username);
    }

    @Test
    public void getSetRegionTest() {
        String region = "Alberta";
        assertNull(player.getRegion());
        player.setRegion(region);
        assertNotNull(player.getRegion());
        assertEquals(player.getRegion(), region);
    }

    @Test
    public void getSetHasScannedTest() {
        int hasScanned = 2506;
        assertEquals(0, player.getHasScanned());
        player.setHasScanned(hasScanned);
        assertEquals(player.getHasScanned(), hasScanned);
    }

    @Test
    public void getSetHighScoreTest() {
        int highScore = 2506;
        assertEquals(0, player.getHighestScore());
        player.setHighestScore(highScore);
        assertEquals(highScore, player.getHighestScore());
    }

    @Test
    public void getSetScoreTest() {
        int score = 2506;
        assertEquals(0, player.getScore());
        player.setScore(score);
        assertEquals(score, player.getScore());
    }
}
